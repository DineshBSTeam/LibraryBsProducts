package com.lib.bslibrary.internal.leads.leadDetail

import ApiRepository
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.lifecycle.ViewModelProvider
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.ActivityLeadDetailBinding
import com.lib.bslibrary.internal.helper.LeadStatusConstants
import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailData
import java.net.URLEncoder

class LeadDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeadDetailBinding

    lateinit var viewModel: LeadDetailViewModel

    private val adapter = LeadDetailListAdapter(onRefresh = {
        viewModel.getLeadDetail(leadId = viewModel.leadId)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLeadDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()

        val mainRepository = ApiRepository(retrofitService)

        viewModel = ViewModelProvider(
            this,
            LeadDetailModelFactory(mainRepository)
        )[LeadDetailViewModel::class.java]

        viewModel.leadId = intent.getIntExtra("leadId", 0)

        viewModel.getLeadDetail(leadId = viewModel.leadId)

        binding.leadsListView.adapter = adapter

        binding.actionBack.setOnClickListener { this.finish() }

        viewModel.leadDetail.observe(this) {
            adapter.setLeadDetail(it)
            bindHeaderData(it)
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

    private fun bindHeaderData(leadDetailData: LeadDetailData) {
        binding.leadCodeText.text = viewModel.leadId.toString()
        Util().loadNetworkImage(
            this,
            leadDetailData.productLogo.toString(),
            binding.balanceView.leadIcon
        )
        binding.balanceView.leadProductName.text = leadDetailData.productTitle ?: ""
        binding.balanceView.infoView.text = (leadDetailData.createdAt ?: "").toString()
        binding.balanceView.remarkStatus.text = leadDetailData.leadStatus

        binding.balanceView.remarkStatus.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                when (leadDetailData.leadStatusId) {
                    LeadStatusConstants.expiredLeadStatus -> R.color.redTextColorExpired
                    LeadStatusConstants.successLeadStatus -> R.color.greenTextColorSuccess
                    LeadStatusConstants.pendingLeadStatus -> R.color.yellowTextColorPending
                    else -> R.color.skyBlueTextColorActionRequired
                }
            )
        )

        binding.balanceView.statusIcon.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                when (leadDetailData.leadStatusId) {
                    LeadStatusConstants.expiredLeadStatus -> R.drawable.ic_clear_solid
                    LeadStatusConstants.successLeadStatus -> R.drawable.ic_check_solid
                    LeadStatusConstants.pendingLeadStatus -> R.drawable.ic_info
                    else -> R.drawable.ic_alert
                }
            )
        )

        binding.balanceView.statusIcon.setColorFilter(
            ContextCompat.getColor(
                this, when (leadDetailData.leadStatusId) {
                    LeadStatusConstants.expiredLeadStatus -> R.color.redTextColorExpired
                    LeadStatusConstants.successLeadStatus -> R.color.greenTextColorSuccess
                    LeadStatusConstants.pendingLeadStatus -> R.color.yellowTextColorPending
                    else -> R.color.skyBlueTextColorActionRequired
                }
            )
        )

        binding.balanceView.statusView.setCardBackgroundColor(
            AppCompatResources.getColorStateList(
                this,
                when (leadDetailData.leadStatusId) {
                    LeadStatusConstants.expiredLeadStatus -> R.color.redBgColorExpired
                    LeadStatusConstants.successLeadStatus -> R.color.greenBgColorSuccess
                    LeadStatusConstants.pendingLeadStatus -> R.color.yellowBgColorPending
                    else -> R.color.skyBlueBgColorActionRequired
                }
            )
        )

        binding.balanceView.customerIc.text = (leadDetailData.customerName ?: "C").toString()
            .first()
            .uppercase()
        binding.balanceView.customerName.text =
            (leadDetailData.customerName ?: "Customer").toString()
        binding.balanceView.custMobile.text = (leadDetailData.mobileNo ?: "N/A").toString()

        val remark = SpannableStringBuilder()
            .bold { append(getString(R.string.remark) + " ") }
            .append(leadDetailData.leadRemark)

        binding.balanceView.remarkLeads.text = remark

        val disclaimer = SpannableStringBuilder()
            .bold { append(getString(R.string.disclaimer) + " ") }
            .append(getString(R.string.disclaimerDesc))

        binding.disclaimerText.text = disclaimer


        binding.balanceView.whSymbol.setOnClickListener {
            val phone = "+91" + Util().toCorrectMobileNo(leadDetailData.mobileNo.toString())
            if (phone.isEmpty()) {
                Toast.makeText(this, "Phone No not available", Toast.LENGTH_SHORT).show()
            }
            val url = "https://api.whatsapp.com/send?phone=$phone&text=" + URLEncoder.encode(
                leadDetailData.leadRemark,
                "UTF-8"
            )
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.balanceView.phSymbol.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.CALL_PHONE),
                    123
                )
            } else {
                val phone = Util().toCorrectMobileNo(leadDetailData.mobileNo.toString())
                if (phone.isEmpty()) {
                    Toast.makeText(this, "Phone No not available", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                startActivity(intent)
            }
        }
    }
}
