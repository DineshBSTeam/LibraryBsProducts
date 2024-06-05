import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.bslibrary.internal.leads.leadlist.models.LeadDisputeQueriesRequest
import com.lib.bslibrary.internal.leads.leadlist.models.LeadQueryData
import kotlinx.coroutines.launch

class TrackQueryViewModel(private val mainRepository: ApiRepository) : ViewModel() {

    val queriesList = MutableLiveData<LeadQueryData>()
    val loading = MutableLiveData<Boolean>(true)
    val buttonLoading = MutableLiveData<Boolean>(false)

    fun getQueriesListData(leadId: Int) {
        viewModelScope.launch {
            val requestBody = LeadDisputeQueriesRequest(leadId)
            when (val response = mainRepository.getChatListData(requestBody)) {
                is NetworkState.Success -> {
                    val responseData = response.data.data
                    responseData?.populateShouldShowDate() // Populate shouldShowDate property
                    queriesList.postValue(responseData)
                    loading.value = false
                }
                is NetworkState.Error -> {
                    loading.value = false
                    // Handle error response
                }
                else -> {}
            }
        }
    }

    fun submitQueryData(
        comment: String,
        leadId: Int,
        queryType: String?,
    ) {
        buttonLoading.value = true
        viewModelScope.launch {
            when (val response = mainRepository.submitQueryData(
                comment,
                queryType,
                leadId,
            )) {
                is NetworkState.Success -> {
                    getQueriesListData(leadId)
                    buttonLoading.value = false
                }
                is NetworkState.Error -> {
                    buttonLoading.value = false
                    // Handle error response
                }
                else -> {
                    buttonLoading.value = false
                }

            }
        }
    }
}

fun LeadQueryData.populateShouldShowDate() {
    var previousDate: String? = null
    queries?.forEach { query ->
        val currentDate = query.createdAt?.substring(4, 10) // Extract the date part
        query.shouldShowDate = currentDate != previousDate
        previousDate = currentDate
    }
}