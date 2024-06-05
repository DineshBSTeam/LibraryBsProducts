package com.lib.bslibrary.internal.products.productDetail.tabs

import ProductMarketingListAdapter
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.lib.bslibrary.databinding.ProductMarketingTabBinding
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.productDetail.ProductDetailViewModel

class ProductMarketingTab() : Fragment() {
    lateinit var contentData: List<String>

    private var _binding: ProductMarketingTabBinding? = null

    private val binding get() = _binding!!

    private val adapter = ProductMarketingListAdapter()

    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProductMarketingTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let {
            viewModel = ViewModelProvider(it)[ProductDetailViewModel::class.java]
        }

        contentData = viewModel.productDetail.value?.tabs?.marketing!!


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (contentData.isEmpty()) {
            binding.emptyDataView.itemView.visibility = View.VISIBLE
        } else {
            binding.emptyDataView.itemView.visibility = View.GONE
        }

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 2
                    else -> 1
                }
            }
        }

        binding.marketingGrid.layoutManager = manager
        val spacingInPixels: Int = Util().dpToPx(8.0)
        binding.marketingGrid.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        binding.marketingGrid.adapter = adapter

        viewModel.errorMessage.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        adapter.setMarketingData(contentData)
    }
    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SpacesItemDecoration(private val mSpacing: Int) : ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = mSpacing
            outRect.top = mSpacing
            outRect.right = mSpacing
            outRect.bottom = mSpacing

        }
    }

}