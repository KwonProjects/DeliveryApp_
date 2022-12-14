package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.review


import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.data.entity.ReviewEntity
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.review.RestaurantReviewRepository
import co.kr.kwon.deliveryapp.model.review.RestaurantReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val reviewStateLiveData =
        MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading
        val result = restaurantReviewRepository.getReviews(restaurantTitle)
        when (result) {
            is Result.Success<*> -> {
                val reviews = result.data as List<ReviewEntity>
                Log.e("data", reviews.toString())
                reviews.sortedWith(compareBy<ReviewEntity>{it.createdAt})
                reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map {
                        RestaurantReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                null
                            } else {
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> Unit
        }

    }
}