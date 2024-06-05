package com.lib.bslibrary.internal.products.productList

import ApiRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.databinding.FragmentProductsBinding
import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.products.productDetail.ProductDetailActivity
import com.lib.bslibrary.internal.products.productList.adapters.CCMainBenefitsAdapter
import com.lib.bslibrary.internal.products.productList.adapters.ProductsCatListAdapter
import com.lib.bslibrary.internal.products.productList.adapters.ProductsListAdapter
import com.bumptech.glide.Glide
import kotlin.system.exitProcess

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding
        get() = _binding!!
    lateinit var productsViewModel: ProductsViewModel
    private lateinit var productsCatListAdapter: ProductsCatListAdapter
    private lateinit var mainBenefitsAdapter: CCMainBenefitsAdapter
    private lateinit var productListAdapter: ProductsListAdapter
    private val selectedCcBenefitIds = ArrayList<Int>()
    private var searchString = ""
    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        handler = Handler(Looper.getMainLooper())
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = ApiRepository(retrofitService)

        productsViewModel =
            ViewModelProvider(
                this,
                ProductsViewModelFactory(mainRepository)
            )[ProductsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
    }

    private fun getSelectedCatPosition(id: Int): Int {
        for (position in 0 until (productsViewModel.productCategories.value?.size ?: 0)) {
            if (productsViewModel.productCategories.value!![position].categoryId == id) {
                return position
            }
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapters() {
        productsCatListAdapter = ProductsCatListAdapter(onClickListener = this::categoryItemClick)
        binding.catView.setHasFixedSize(true)
        binding.catView.adapter = productsCatListAdapter

        mainBenefitsAdapter = CCMainBenefitsAdapter(onClickListener = this::updateBenefitsFilter)
        binding.benefitsView.adapter = mainBenefitsAdapter

        productListAdapter = ProductsListAdapter(onClickListener = this::productClickListener)
        binding.productsListView.adapter = productListAdapter

        addObservers()
        addListeners()

        if (productsViewModel.productCategories.value == null) {
            productsViewModel.getProductCategories()
        } else {
            productsViewModel.productCategories.value?.let {
                productsCatListAdapter.setCategories(it.toMutableList())
                if (productsViewModel.selectedCategoryId == 0) {
                    productsViewModel.selectedCategoryId = it[0].categoryId
                }
                productsCatListAdapter.selectedPosition = getSelectedCatPosition(productsViewModel.selectedCategoryId)

                productsCatListAdapter.notifyDataSetChanged()
            }

            productsViewModel.productList.value?.toMutableList()
                ?.let { productListAdapter.setProductsList(it) }
        }
        val t = Thread {
            binding.catView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(activity!!).resumeRequests()
                    }
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        Glide.with(activity!!).pauseRequests()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
        t.start()
    }

    private fun addListeners() {

        binding.actionBack.setOnClickListener {
            activity!!.finish()
            // on below line we are exiting our activity
            exitProcess(0)
        }

        binding.searchView.setOnSearchClickListener {
            if (binding.searchView.visibility == View.VISIBLE) {
                binding.titleText.visibility = View.GONE
            }
        }

        binding.searchView.setOnCloseListener {
            binding.titleText.visibility = View.VISIBLE
            searchString = ""
            productsViewModel.getProductListing(searchString)
            false
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchString = newText.toString()
                if (searchString.isNotEmpty()) {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        productsViewModel.searchProducts(searchString)
                    }, 300)

                    return true
                }
                return false
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addObservers() {

        productsViewModel.productCategories.observe(this) {
            productsCatListAdapter.setCategories(it)
            productsCatListAdapter.notifyDataSetChanged()
            getProductList()
        }

        productsViewModel.productList.observe(this) {

            val oldPos = productsCatListAdapter.selectedPosition
            productsCatListAdapter.selectedPosition = getSelectedCatPosition(productsViewModel.selectedCategoryId)
            productsCatListAdapter.notifyItemChanged(oldPos)
            productsCatListAdapter.notifyItemChanged(productsCatListAdapter.selectedPosition)

            productListAdapter.setProductsList(it)
            productsViewModel.loading.value = false

            if (it.isEmpty()) {
                binding.emptyDataView.itemView.visibility = View.VISIBLE
            } else {
                binding.emptyDataView.itemView.visibility = View.GONE
            }
        }

        productsViewModel.cardBenefitsList.observe(this) {
            mainBenefitsAdapter.setItemList(it)
        }

        productsViewModel.errorMessage.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        productsViewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }

    }

    private fun getProductList() {
        productsViewModel.productCategories.value?.get(0)?.let { it1 ->
            if (productsViewModel.productList.value == null || productsViewModel.productList.value?.size == 0) {
                productsViewModel.getProductListing("")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun categoryItemClick(catId: Int) {
        if (productsViewModel.selectedCategoryId != catId) {
            productsViewModel.selectedCategoryId = catId
            selectedCcBenefitIds.clear()
            resetSearchView()
            productsViewModel.getProductListing("")
        }
    }

    private fun resetSearchView() {
        if (!binding.searchView.isIconified) {
            binding.searchView.setQuery("", false)
            binding.searchView.isIconified = true
            binding.searchView.onActionViewCollapsed()
        }
    }

    override fun onPause() {
        super.onPause()
        resetSearchView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun productClickListener(selectedCatIndex: Int) {
        startActivity(Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(
                "productId",
                productsViewModel.productList.value?.get(selectedCatIndex)?.productId
            )
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateBenefitsFilter(benefitId: Int) {
        productsViewModel.loading.value = true
        //TODO: implement filter logic according to selections in product list
        if (selectedCcBenefitIds.contains(benefitId)) {
            selectedCcBenefitIds.remove(benefitId)
        } else {
            selectedCcBenefitIds.add(benefitId)
        }
        mainBenefitsAdapter.setSelectedCcBenefitIds(selectedCcBenefitIds)

        val selectedIds = selectedCcBenefitIds.toMutableList()

        if (selectedIds.isEmpty()) {
            productsViewModel.productList.postValue(productsViewModel.copyProductList.value!!)
        } else {
            val list = productsViewModel.copyProductList.value!!.filter { product ->
                selectedIds.all {
                    product.benefits.contains(
                        it
                    )
                }
            }
            productsViewModel.productList.postValue(
                list
            )
        }
        productsViewModel.loading.value = false
    }

}
