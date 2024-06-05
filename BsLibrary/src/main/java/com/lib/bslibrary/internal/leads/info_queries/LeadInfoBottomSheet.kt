package com.lib.bslibrary.internal.leads.info_queries

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.fragment.app.DialogFragment
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.LeadInfoBottomsheetBinding
import com.lib.bslibrary.internal.leads.trackQuery.TrackQueryActivity
import com.lib.bslibrary.internal.helper.LeadStatusConstants
import com.lib.bslibrary.internal.leads.leadlist.models.Lead
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias OnQuerySubmitListener = () -> Unit

class LeadInfoBottomSheet(
    lead: Lead,
    private val tapOn: Int,
    private val onQuerySubmitListener: OnQuerySubmitListener
) : BottomSheetDialogFragment() {
    private val leadData: Lead = lead
    private var _binding: LeadInfoBottomsheetBinding? = null

    private val binding get() = _binding!!
    lateinit var addQueryBottomSheet: AddQueryBottomSheet
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LeadInfoBottomsheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        //header title
        val disclaimer = SpannableStringBuilder()
            .bold { append(getString(R.string.disclaimer) + " ") }
            .append(getString(R.string.disclaimerDesc))

        binding.titleText.text = disclaimer

        //conditional visibility & BIND content
        _binding!!.crSubtitle.text = leadData.createdDateSubTitle ?: ""
        _binding!!.luSubtitle.text = leadData.lastUpdatedDateSubTitle ?: ""
        _binding!!.nxSubtitle.text = leadData.nextUpdateSubTitle ?: ""

        _binding!!.createdAtLayout.visibility =
            if (tapOn >= LeadStatusConstants.createdAtInfo) View.VISIBLE else View.GONE
        _binding!!.lastUpdateLayout.visibility =
            if (tapOn >= LeadStatusConstants.updatedAtInfo) View.VISIBLE else View.GONE
        _binding!!.nextUpdateLayout.visibility =
            if (tapOn == LeadStatusConstants.nextUpdateInfo) View.VISIBLE else View.GONE
        _binding!!.queryLayout.visibility =
            if (tapOn >= LeadStatusConstants.haveIssueInfo) View.VISIBLE else View.GONE

        if (tapOn == LeadStatusConstants.haveIssueInfo) {
            _binding!!.queryBottomLine.visibility = View.GONE
        } else if (tapOn == LeadStatusConstants.nextUpdateInfo) {
            _binding!!.nxBottomLine.visibility = View.GONE
        } else if (tapOn == LeadStatusConstants.updatedAtInfo) {
            _binding!!.luBottomLine.visibility = View.GONE
        } else {
            _binding!!.bottomLine.visibility = View.GONE
        }
        //query view text conditionally
        if (tapOn >= LeadStatusConstants.haveIssueInfo) {
            _binding!!.haveIssue.text =
                if (!leadData.isQuery || !leadData.isQueryActive) context?.getString(R.string.haveIssue)
                else context?.getString(R.string.trackYourQuery)

            _binding!!.raiseQuery.text = if (!leadData.isQuery || !leadData.isQueryActive)
                context?.getString(R.string.raiseQuery)
            else context?.getString(R.string.checkStatus)
        }

        //close button
        _binding!!.sheetCloser.setOnClickListener {
            dismiss()
        }
        //raise query open
        _binding!!.raiseQueryView.setOnClickListener {
            if (!leadData.isQuery || !leadData.isQueryActive) {
                addQueryBottomSheet =
                    AddQueryBottomSheet(leadData.leadId ?: 0, onClickListener = {
                        addQueryBottomSheet.dismiss()
                        onQuerySubmitListener()
                    })
                addQueryBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                addQueryBottomSheet.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    "LeadAddQueryBottomSheet"
                )
            } else {
                val i = Intent(context, TrackQueryActivity::class.java)
                i.putExtra("LEAD_ID", leadData.leadId)
                startActivity(i)
            }
        }
    }
}