package com.lib.bslibrary.internal.products.productDetail

import AdapterViewPager
import ApiRepository
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lib.bslibrary.databinding.ActivityProductDetailBinding
import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.productDetail.tabs.ProductDetailTab
import com.lib.bslibrary.internal.products.productDetail.tabs.ProductMarketingTab
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    lateinit var viewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()

        val mainRepository = ApiRepository(retrofitService)

        viewModel = ViewModelProvider(
            this,
            ProductDetailModelFactory(mainRepository)
        )[ProductDetailViewModel::class.java]

        viewModel.productId = intent.getIntExtra("productId", 0)

        viewModel.getProductDetail(productId = viewModel.productId)

        binding.actionBack.setOnClickListener { this.finish() }

        viewModel.productDetail.observe(this) {
            bindHeaderData()
            initiateViewPager()

        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
    }

    private fun bindHeaderData() {
        val it = viewModel.productDetail.value
        Util().loadNetworkImage(this, it?.productLogo.toString(), binding.productLogo)
        binding.productName.text = it?.productTitle ?: ""
        binding.productSubText.text = it?.productSubTitle ?: ""
    }

    private fun initiateViewPager() {
        val adapter = AdapterViewPager(this)
        //check if both tab has NoData
        if((viewModel.productDetail.value?.tabs?.details == null || viewModel.productDetail.value?.tabs?.details?.size == 0) && (viewModel.productDetail.value?.tabs?.marketing == null || viewModel.productDetail.value?.tabs?.marketing?.size == 0)){
            binding.tabView.visibility = View.GONE
            binding.pager.visibility = View.GONE
            binding.emptyDataView.itemView.visibility = View.VISIBLE
        }else{
            binding.emptyDataView.itemView.visibility = View.GONE
            //any one is missing :- tabview hide
            if (viewModel.productDetail.value?.tabs?.details == null || viewModel.productDetail.value?.tabs?.details?.size == 0 ||
                viewModel.productDetail.value?.tabs?.marketing == null || viewModel.productDetail.value?.tabs?.marketing?.size == 0
            ) {
                binding.tabView.visibility = View.GONE
            }else{
                binding.tabView.visibility = View.VISIBLE
            }

            //Check with which fragment need to add
            if (viewModel.productDetail.value?.tabs?.details != null && viewModel.productDetail.value?.tabs?.details?.size != 0) {
                adapter.addFragment(ProductDetailTab(), "Details")
            }
            if (viewModel.productDetail.value?.tabs?.marketing != null && viewModel.productDetail.value?.tabs?.marketing?.size != 0) {
                adapter.addFragment(ProductMarketingTab(), "Marketing")
            }

            binding.pager.adapter = adapter
            binding.pager.currentItem = 0
            if (viewModel.productDetail.value?.tabs?.details != null && viewModel.productDetail.value?.tabs?.marketing != null) {
                TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                    tab.text = adapter.getTabTitle(position)
                }.attach()
            }
        }
    }
}