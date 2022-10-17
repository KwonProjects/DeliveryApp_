package co.kr.kwon.deliveryapp.screen.main.my

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.preference.AppPreferenceManager
import co.kr.kwon.deliveryapp.data.repository.order.OrderRepository
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.review.RestaurantReviewRepository
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel
import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val reviewRepository: RestaurantReviewRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    val myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)

    val myReviewWrittenMutableLiveData = MutableLiveData<List<UnWrittenReviewModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = MyState.Loading
        appPreferenceManager.getIdToken()?.let {
            Log.e("MyViewModel",it.toString())
            myStateLiveData.value = MyState.Login(it)
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }



    fun setUserInfo(firebase: FirebaseUser?) = viewModelScope.launch {
        firebase?.let { user ->
            when (val orderMenusResult = orderRepository.getAllOrderMenus(user.uid)) {
                is Result.Success<*> -> {
                    myStateLiveData.value = MyState.Success.Registered(
                        userName = user.displayName ?: "익명",
                        userEmail = user.email.toString(),
                        profileImageUri = user.photoUrl
                    )
                }
                is Result.Error ->{
                    myStateLiveData.value = MyState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                    Log.e("MyViewModel","error : ${orderMenusResult.e}")
                }
            }
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }


    fun unReviewCheckAndSetUserInfo(firebase: FirebaseUser?) = viewModelScope.launch {
        firebase?.let { user ->
            when (val reviewResult = reviewRepository.unWrittenReview(user.uid)) {
                is Result.Success<*> -> {
                    val reviewList : List<OrderEntity>  = reviewResult.data as List<OrderEntity>
                    Log.e("MyViewModel","size : ${reviewList.size}")
                    myStateLiveData.value = MyState.Success.checkReview(
                        userName = user.displayName ?: "익명",
                        userEmail = user.email.toString(),
                        profileImageUri = user.photoUrl,
                        reviewList.map {review ->
                            UnWrittenReviewModel(
                                date = review.date,
                                id = review.hashCode().toLong(),
                                orderId = review.id,
                                userId = review.userId,
                                restaurantId = review.restaurantId,
                                foodMenuList = review.foodMenuList,
                                writeReview = review.writeReview
                            )
                        }
                    )
                }
                is Result.Error -> {
                    myStateLiveData.value = MyState.Error(
                        R.string.request_error,
                        reviewResult.e
                    )
                    Log.e("MyViewModel","error : ${reviewResult.e}")
                }
            }
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }

    }


    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.removeIdToken()
        }
     //   userRepository.deleteAllUserLikedRestaurant()
        fetchData()
    }

}