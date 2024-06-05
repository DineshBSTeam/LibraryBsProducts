package com.lib.bslibrary.internal.products.productList.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.AdapterCategoryListBinding
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.productList.models.ProductCategoriesData

typealias MyCategoryClickListener = (Int) -> Unit

class ProductsCatListAdapter(
    private val onClickListener: MyCategoryClickListener
) : RecyclerView.Adapter<ProductCatViewHolder>() {
    var selectedPosition = 0
    private var productCatList = mutableListOf<ProductCategoriesData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(Products: List<ProductCategoriesData>) {
        this.productCatList = Products.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterCategoryListBinding.inflate(inflater, parent, false)
        return ProductCatViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ProductCatViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val param = holder.binding.mainView.layoutParams as ViewGroup.MarginLayoutParams
        param.marginStart = if (position == 0) 30 else 15
        param.marginEnd = if (position == productCatList.size - 1) 30 else 15
        holder.binding.mainView.layoutParams = param
        val data = productCatList[position]

        Util().loadNetworkImage(holder.itemView.context, data.categoryIcon, holder.binding.iconView)

        holder.binding.title.text = data.categoryName

        if (data.earnUpto != null)
            holder.binding.subTitle.visibility = View.VISIBLE
        else
            holder.binding.subTitle.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val catId = data.categoryId
            onClickListener(catId)
        }

        if (selectedPosition == position) {
            holder.binding.mainView.background =
                AppCompatResources.getDrawable(context, R.drawable.cat_card_border_selected)
            holder.binding.iconView.setColorFilter(ContextCompat.getColor(context, R.color.white))
            holder.binding.title.setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.white
                )
            )
            holder.binding.subTitle.setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.white
                )
            )

        } else {
            holder.binding.mainView.background =
                AppCompatResources.getDrawable(
                    context, R.drawable.cat_card_border
                )
            holder.binding.iconView.setColorFilter(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
            holder.binding.title.setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.black
                )
            )
            holder.binding.subTitle.setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.blueTextColor
                )
            )

        }
    }

    override fun getItemCount(): Int {
        return productCatList.size
    }
}

class ProductCatViewHolder(val binding: AdapterCategoryListBinding) :
    RecyclerView.ViewHolder(binding.root)