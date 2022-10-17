package co.kr.kwon.deliveryapp.screen.main.orderhistory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.data.preference.AppPreferenceManager
import co.kr.kwon.deliveryapp.data.repository.food.RestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.order.OrderRepository
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import co.kr.kwon.deliveryapp.data.repository.restaurant.RestaurantRepository
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderHistoryListViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository,
) : BaseViewModel() {

    val orderHistoryListLiveData =
        MutableLiveData<OrderHistoryListState>(OrderHistoryListState.Uninitialized)


    override fun fetchData(): Job = viewModelScope.launch {
        orderHistoryListLiveData.value = OrderHistoryListState.Loading
        appPreferenceManager.getIdToken()?.let {
            orderHistoryListLiveData.value = OrderHistoryListState.Login(it)
        } ?: kotlin.run {
            orderHistoryListLiveData.value = OrderHistoryListState.Success.NotRegistered
        }
    }

    fun setUserInfo(firebaseAuth: FirebaseAuth?) = viewModelScope.launch {
        firebaseAuth?.currentUser?.let { user ->
            when (val orderMenusResult = orderRepository.getAllOrderMenus(user.uid)) {
                is Result.Success<*> -> {
                    val orderList = orderMenusResult.data as List<OrderEntity>
                    orderList.sortedWith(compareBy<OrderEntity> {it.date})
                    orderHistoryListLiveData.value = OrderHistoryListState.Success.Registered(
                        orderList = orderList.map {
                            OrderHistoryModel(
                                date = it.date,
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                writeReview = it.writeReview
                            )
                        }
                    )
                }
                is Result.Error -> {
                    orderHistoryListLiveData.value = OrderHistoryListState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                    Log.e("MyViewModel", "error : ${orderMenusResult.e}")
                }
            }
        } ?: kotlin.run {
            orderHistoryListLiveData.value = OrderHistoryListState.Success.NotRegistered
        }
    }
    fun reOrder(foodMenuList : List<RestaurantFoodEntity>) = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        restaurantFoodRepository.insertFoodsMenuInBasket(foodMenuList)
    }

}