package com.lib.bslibrary.internal.products.productList.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.databinding.AdapterBenefitsFlexBinding
import com.google.android.flexbox.FlexboxLayoutManager

class BenefitsListAdapter : RecyclerView.Adapter<BenefitsViewHolder>() {

    var cardBenefits = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setfeaturesList(Products: List<String>) {
        this.cardBenefits = Products.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterBenefitsFlexBinding.inflate(inflater, parent, false)
        return BenefitsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BenefitsViewHolder, position: Int) {
        val pos = position % cardBenefits.size
        holder.bindTo(cardBenefits[pos])
    }

    override fun getItemCount(): Int {
        return cardBenefits.size
    }
}

class BenefitsViewHolder(val binding: AdapterBenefitsFlexBinding) : RecyclerView.ViewHolder(binding.root) {

    internal fun bindTo(text:String) {
        binding.benefitText.setText(text)
        val lp = binding.benefitText.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1f
        }
    }
}