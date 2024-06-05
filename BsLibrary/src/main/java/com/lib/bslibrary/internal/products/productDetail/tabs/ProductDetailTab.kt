package com.lib.bslibrary.internal.products.productDetail.tabs

import DetailPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lib.bslibrary.databinding.ProductDetailTabBinding
import com.lib.bslibrary.internal.products.productDetail.ProductDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailTab : Fragment() {

    private var _binding: ProductDetailTabBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailTabBinding.inflate(inflater, container, false)
        val root: View = binding.root
        activity?.let {
            viewModel = ViewModelProvider(it)[ProductDetailViewModel::class.java]
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.productDetail.value?.tabs?.details != null) {
            initiateViewPager()
        }else{
            binding.emptyDataView.itemView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
    private fun initiateViewPager() {
        val adapter = DetailPagerAdapter(activity)
        for (k in 0 until (viewModel.productDetail.value?.tabs?.details?.size ?: 0)) {
            binding.emptyDataView.itemView.visibility = View.GONE
            viewModel.productDetail.value?.tabs?.details?.get(k)
                ?.let { adapter.addFragment(ProductDetailTabContent(it.content), it.tabName) }
        }

        binding.detailPager.isNestedScrollingEnabled = false
        binding.detailPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.detailPager.adapter = adapter
        binding.detailPager.currentItem = 0

        if (viewModel.productDetail.value?.tabs?.details != null && viewModel.productDetail.value?.tabs?.details?.size != 0) {
            TabLayoutMediator(binding.tabLayout, binding.detailPager) { tab, position ->
                tab.text = adapter.getTabTitle(position)
            }.attach()
        }else{
            binding.tabLayout.visibility = View.GONE
        }

        binding.detailPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
}