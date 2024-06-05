package com.lib.bslibrary.internal.leads.leadlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.AdapterLeadListBinding
import com.lib.bslibrary.internal.helper.LeadStatusConstants
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.leads.info_queries.LeadInfoBottomSheet
import com.lib.bslibrary.internal.leads.leadDetail.LeadDetailActivity
import com.lib.bslibrary.internal.leads.leadlist.models.Lead
import com.lib.bslibrary.internal.leads.leadlist.models.LeadListingData

typealias OnQuerySubmitListener = () -> Unit

class LeadsListAdapter(val onQuerySubmitListener: OnQuerySubmitListener) :
    RecyclerView.Adapter<LeadsViewHolder>() {

    var leadsList = mutableListOf<Lead?>()
    lateinit var leadInfoBottomSheet : LeadInfoBottomSheet

    @SuppressLint("NotifyDataSetChanged")
    fun setLeadListing(leads: LeadListingData) {
        this.leadsList = leads.leads?.data?.toMutableList()!!
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMoreLeadListing(leads: LeadListingData) {
        leads.leads?.data?.let { this.leadsList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterLeadListBinding.inflate(inflater, parent, false)
        return LeadsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeadsViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val data = leadsList[position]

        Util().loadNetworkImage(context, data?.productLogo.toString(), holder.binding.imageview)
        holder.binding.title.text = data?.productTitle ?: ""
        holder.binding.subtitle.text = (data?.mobileNo ?: "").toString()

        holder.binding.subtrailing.text = data?.createdAt?.split(' ')?.get(0) ?: ""
        holder.binding.lastUpdateDate.text = data?.lastUpdatedDate?.split(' ')?.get(0) ?: ""
        holder.binding.nextUpdateDate.text = data?.nextUpdate?.split(' ')?.get(0) ?: ""
        holder.binding.remarkStatus.text = data?.leadStatus
        holder.binding.remark.text = data?.leadRemark

        holder.binding.remarkStatus.setTextColor(
            AppCompatResources.getColorStateList(
                context,
                when (data?.leadStatusParentId) {
                    LeadStatusConstants.expiredLeadStatus -> R.color.redTextColorExpired
                    LeadStatusConstants.successLeadStatus -> R.color.greenTextColorSuccess
                    LeadStatusConstants.pendingLeadStatus -> R.color.yellowTextColorPending
                    else -> R.color.skyBlueTextColorActionRequired
                }
            )
        )

        holder.binding.remarkView.setCardBackgroundColor(
            AppCompatResources.getColorStateList(
                context,
                when (data?.leadStatusParentId) {
                    LeadStatusConstants.expiredLeadStatus -> R.color.redBgColorExpired
                    LeadStatusConstants.successLeadStatus -> R.color.greenBgColorSuccess
                    LeadStatusConstants.pendingLeadStatus -> R.color.yellowBgColorPending
                    else -> R.color.skyBlueBgColorActionRequired
                }
            )
        )

        holder.binding.raiseQueryView.visibility =
            if (data?.isDisputeVisible == true) View.VISIBLE else View.GONE
        holder.binding.nextUpdateInfo.visibility =
            if (data?.isDisputeVisible == false) View.VISIBLE else View.GONE

        holder.binding.haveIssue.text = if (data?.isQuery == false ||
            data?.isQueryActive == false
        ) context.getString(R.string.haveIssue)
        else context.getString(R.string.trackYourQuery)

        holder.binding.raiseQuery.text = if (data?.isQuery == false ||
            data?.isQueryActive == false
        )
            context.getString(R.string.raiseQuery)
        else context.getString(R.string.checkStatus)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, LeadDetailActivity::class.java).apply {
                putExtra("leadId", data?.leadId)
            })
        }

        holder.binding.createdAtInfo.setOnClickListener {
            if (data != null) {
                showInfo(context, data, 1)
            }
        }
        holder.binding.lastUpdateInfo.setOnClickListener {
            if (data != null) {
                showInfo(context, data, 2)
            }
        }
        holder.binding.nextUpdateInfo.setOnClickListener {
            if (data != null) {
                showInfo(context, data, 3)
            }
        }
        holder.binding.raiseQueryView.setOnClickListener {
            if (data != null) {
                showInfo(context, data, 4)
            }
        }
        holder.binding.arrowButton.setOnClickListener {
            if (data != null) {
                showInfo(context, data, 4)
            }
        }
    }

    override fun getItemCount(): Int {
        return leadsList.size
    }

    private fun showInfo(context: Context, data: Lead, tapOn: Int) {
        leadInfoBottomSheet = LeadInfoBottomSheet(data, tapOn, onQuerySubmitListener = {
            leadInfoBottomSheet.dismiss()
            onQuerySubmitListener()
        })
        leadInfoBottomSheet.show(
            (context as AppCompatActivity).supportFragmentManager,
            "LeadInfoBottomSheet"
        )
    }

}

class LeadsViewHolder(val binding: AdapterLeadListBinding) : RecyclerView.ViewHolder(binding.root) {
}
