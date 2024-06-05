package com.lib.bslibrary.internal.leads.leadDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.AdapterLeadDetailListBinding
import com.lib.bslibrary.internal.leads.trackQuery.TrackQueryActivity
import com.lib.bslibrary.internal.leads.info_queries.AddQueryBottomSheet
import com.lib.bslibrary.internal.leads.leadDetail.models.JourneyObj
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailData
import com.lib.bslibrary.internal.leads.leadlist.models.*

typealias OnRefresh = () -> Unit

class LeadDetailListAdapter(val onRefresh: OnRefresh) :
    RecyclerView.Adapter<LeadsDetailViewHolder>() {

    private var leadDetailData = MutableLiveData<LeadDetailData>()
    private var journeyList = mutableListOf<JourneyObj?>()
    lateinit var addQueryBottomSheet: AddQueryBottomSheet

    @SuppressLint("NotifyDataSetChanged")
    fun setLeadDetail(leads: LeadDetailData?) {
        leadDetailData.postValue(leads)
        val list = leads?.journey
        if (list != null) {
            this.journeyList = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadsDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterLeadDetailListBinding.inflate(inflater, parent, false)
        return LeadsDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeadsDetailViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val leadData = journeyList[position]
        holder.binding.title.text = leadData?.leadStatusTitle ?: ""
        holder.binding.subStatus.text = (leadData?.leadSubStatus ?: "").toString()
        holder.binding.subStatus.visibility =
            if (leadData?.leadSubStatus.isNullOrBlank()) View.GONE else View.VISIBLE

        holder.binding.subtitle.text = (leadData?.leadSubRemark ?: "").toString()
        holder.binding.subtrailing.text = leadData?.updatedAt ?: ""

        holder.binding.upperLine.setColorFilter(
            ContextCompat.getColor(
                context,
                if (position == 0) R.color.pageBackgroundColor
                else R.color.black
            )
        )

        holder.binding.bottomLine.setColorFilter(
            ContextCompat.getColor(
                context,
                if (position == journeyList.size - 1) R.color.pageBackgroundColor
                else R.color.black
            )
        )

        holder.binding.raiseQueryView.visibility =
            if ((leadDetailData.value?.isDisputeVisible == true) && position == journeyList.size - 1
            ) View.VISIBLE else View.GONE

        if (holder.binding.raiseQueryView.visibility == View.VISIBLE) {
            holder.binding.haveIssue.text = if (leadDetailData.value?.isQuery == false ||
                leadDetailData.value?.isQueryActive == false
            ) context.getString(R.string.haveIssue)
            else context.getString(R.string.trackYourQuery)

            holder.binding.raiseQuery.text = if (leadDetailData.value?.isQuery == false ||
                leadDetailData.value?.isQueryActive == false
            ) context.getString(R.string.raiseQuery)
            else context.getString(R.string.checkStatus)
        }

        holder.binding.raiseQueryView.setOnClickListener {
            if (leadDetailData.value?.isQuery == false || leadDetailData.value?.isQueryActive == false) {
                addQueryBottomSheet =
                    AddQueryBottomSheet(leadDetailData.value?.leadId ?: 0, onClickListener = {
                        addQueryBottomSheet.dismiss()
                        onRefresh()
                    })
                addQueryBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                addQueryBottomSheet.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    "LeadAddQueryBottomSheet"
                )
            } else {
                val intent = Intent(context, TrackQueryActivity::class.java)
                intent.putExtra("LEAD_ID", leadDetailData.value?.leadId)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return journeyList.size
    }
}

class LeadsDetailViewHolder(val binding: AdapterLeadDetailListBinding) :
    RecyclerView.ViewHolder(binding.root) {
}