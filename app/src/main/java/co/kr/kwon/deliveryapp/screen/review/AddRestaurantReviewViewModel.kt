package co.kr.kwon.deliveryapp.screen.review

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.review.RestaurantReviewRepository
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.screen.main.home.HomeState
import co.kr.kwon.deliveryapp.screen.order.OrderMenuState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddRestaurantReviewViewModel(
    private val orderId: String,
    private val reviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val addRestaurantReviewStateLiveData =
        MutableLiveData<AddRestaurantReviewState>(AddRestaurantReviewState.Uninitialized)


    override fun fetchData(): Job = viewModelScope.launch {

    }

    fun writeReview(doc: String) = viewModelScope.launch {

        val currentTime = SimpleDateFormat(
            "yyyy-MM-dd kk:mm:ss E",
            Locale("ko", "KR")
        ).format(Date(System.currentTimeMillis()))
        when (val data = reviewRepository.writeReview(doc,currentTime)) {
            is Result.Success<*> -> {
                addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Review
            }
            is Result.Error -> {
                addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Error(
                    R.string.request_error, data.e
                )
            }
        }
    }
}