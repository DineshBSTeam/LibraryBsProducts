package com.lib.bslibrary.internal.products.productList.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.AdapterProductsListBinding
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.ShareBottomSheet
import com.lib.bslibrary.internal.products.productList.models.ProductListingData
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.*

typealias OnProductClickListener = (Int) -> Unit

class ProductsListAdapter(
    private val onClickListener: OnProductClickListener
) : RecyclerView.Adapter<ProductsViewHolder>() {
    private var productsList = mutableListOf<ProductListingData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductsList(Products: List<ProductListingData>) {
        this.productsList = Products.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterProductsListBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val data = productsList[position]

        //Benefits view Binding for List Type
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        holder.binding.featuresListView.layoutManager = layoutManager
        val adapter = BenefitsListAdapter()
        holder.binding.featuresListView.adapter = adapter
        adapter.setfeaturesList(productsList[position].features)
        holder.binding.featuresListView.visibility =
            if (productsList[position].features.isEmpty()) View.GONE else View.VISIBLE

        //main items View Binding
        Util().loadNetworkImage(context, data.productLogo, holder.binding.imageview)

        holder.binding.title.text = data.productTitle
        holder.binding.subtitle.text = data.productSubTitle
        holder.binding.anualFee.text = data.annualFee
        holder.binding.joiningFee.text = data.joiningFee
        holder.binding.feesView.visibility =
            if (data.annualFee != null) View.VISIBLE else View.GONE

        if (data.earnUpto != null) {
            holder.binding.earnView.visibility = View.VISIBLE
            holder.binding.spacer.visibility = View.VISIBLE
            holder.binding.shareText.text = context.getString(R.string.share)

        } else {
            holder.binding.earnView.visibility = View.GONE
            holder.binding.spacer.visibility = View.GONE
            holder.binding.shareText.text = context.getString(R.string.shareWithCustomer)
        }
        holder.itemView.setOnClickListener {
            onClickListener(position,)
        }

        holder.binding.footerView1.setOnClickListener{
            val bottomSheetDialogFragment = ShareBottomSheet(
                data.shareData,
                "",
                false
            )

            bottomSheetDialogFragment.show(
                (context as AppCompatActivity).supportFragmentManager,
                "ShareBottomSheet"
            )
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}

class ProductsViewHolder(val binding: AdapterProductsListBinding) :
    RecyclerView.ViewHolder(binding.root) {
}
