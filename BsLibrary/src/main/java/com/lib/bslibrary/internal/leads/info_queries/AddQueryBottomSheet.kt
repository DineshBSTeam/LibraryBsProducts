package com.lib.bslibrary.internal.leads.info_queries

import ApiRepository
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.AddQueryBottomsheetBinding
import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.leads.info_queries.models.QueryType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias OnClickListener = () -> Unit

class AddQueryBottomSheet(private val leadId: Int, private val onClickListener: OnClickListener) : BottomSheetDialogFragment() {
    private var _binding: AddQueryBottomsheetBinding? = null

    private val binding get() = _binding!!

    lateinit var viewModel: AddQueryViewModel
    var selectedQueryType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddQueryBottomsheetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = ApiRepository(retrofitService)

        selectedQueryType = context!!.getString(R.string.selectReason)

        viewModel = ViewModelProvider(
            this,
            AddQueryModelFactory(mainRepository)
        )[AddQueryViewModel::class.java]

        viewModel.getLeadDisputeOptions()

        viewModel.leadDisputeOptions.observe(this) {
            viewModel.disputeOptionsList =
                (viewModel.leadDisputeOptions.value?.data!!).toMutableList()
            viewModel.disputeOptionsList.add(
                0,
                QueryType(context?.getString(R.string.selectReason))
            )
            bindSpinnerAdapter(_binding!!)
        }

        binding.queryInputBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _binding!!.queryTextView.background = ContextCompat.getDrawable(activity!!, R.drawable.border_query_dropdown)
            }
        })

        viewModel.errorMessage.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
        viewModel.isSubmitted.observe(this) {
            if (it) {
                Toast.makeText(context!!,"Query Submitted Successfully",Toast.LENGTH_SHORT).show()
                onClickListener()
            } else {
                Toast.makeText(
                    context,
                    "Something went wrong, Try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        _binding!!.submitQueryButton.setOnClickListener {
            if (selectedQueryType == context!!.getString(R.string.selectReason)) {
                Toast.makeText(
                    context,
                    context!!.getString(R.string.selectReason),
                    Toast.LENGTH_SHORT
                ).show()
                _binding!!.queryTypeView.background = ContextCompat.getDrawable(activity!!, R.drawable.error_border_query_dropdown)
            } else if (_binding!!.queryInputBox.text.toString().isNotEmpty()) {
                    viewModel.submitLeadDisputeQuery(
                        _binding!!.queryInputBox.text.toString(),
                        selectedQueryType,
                        leadId,
                        null
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Write your Query",
                        Toast.LENGTH_SHORT
                    ).show()
                    _binding!!.queryTextView.background = ContextCompat.getDrawable(activity!!, R.drawable.error_border_query_dropdown)
                }
        }
        return root
    }

    private fun bindSpinnerAdapter(_binding: AddQueryBottomsheetBinding) {
        val adapter: ArrayAdapter<QueryType> = ArrayAdapter<QueryType>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            viewModel.disputeOptionsList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding.queryTypeSpinner.adapter = adapter

        _binding.queryTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    _binding.queryTypeView.background = ContextCompat.getDrawable(activity!!, R.drawable.border_query_dropdown)
                    selectedQueryType = (parent.selectedItem as QueryType).queryType.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

}