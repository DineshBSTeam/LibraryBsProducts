package com.lib.bslibrary.internal.products.productList.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.MainBenefitItemBinding
import com.lib.bslibrary.internal.products.productList.models.CreditCardBenefits

typealias ChipSelectionListener = (Int) -> Unit

class CCMainBenefitsAdapter(
    private val onClickListener: ChipSelectionListener
) : RecyclerView.Adapter<MainBenefitsViewHolder>() {
    private var selectedCcBenefitIds = ArrayList<Int>()

    private var cardBenefitsList = mutableListOf<CreditCardBenefits>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(Products: List<CreditCardBenefits>) {
        this.cardBenefitsList = Products.toMutableList()
        notifyDataSetChanged()
    }
  @SuppressLint("NotifyDataSetChanged")
    fun setSelectedCcBenefitIds(ids: ArrayList<Int>) {
        this.selectedCcBenefitIds = ids
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBenefitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainBenefitItemBinding.inflate(inflater, parent, false)
        return MainBenefitsViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MainBenefitsViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val param = holder.binding.mainView.layoutParams as ViewGroup.MarginLayoutParams
        param.marginStart = if (position == 0) 30 else 15
        param.marginEnd = if (position == cardBenefitsList.size - 1) 30 else 15
        holder.binding.mainView.layoutParams = param
        val data = cardBenefitsList[position]

        if (selectedCcBenefitIds.contains(data.id)) {
            holder.binding.mainView.background =
                AppCompatResources.getDrawable(context, R.drawable.card_benefits_border_selected)
            holder.binding.clearIconView.visibility = View.VISIBLE
        } else {
            holder.binding.mainView.background =
                AppCompatResources.getDrawable(
                    context, R.drawable.card_benefits_border
                )
            holder.binding.clearIconView.visibility = View.GONE
        }

        holder.binding.title.text = data.title

        holder.itemView.setOnClickListener {
            onClickListener(data.id)
        }

    }

    override fun getItemCount(): Int {
        return cardBenefitsList.size
    }
}

class MainBenefitsViewHolder(val binding: MainBenefitItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

}