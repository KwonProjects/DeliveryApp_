package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.data.repository.food.RestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.order.OrderRepository
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.review.RestaurantReviewRepository
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailState
import co.kr.kwon.deliveryapp.util.date.ConvertDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderEntity: OrderEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val reviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val orderDetailStateLiveData =
        MutableLiveData<OrderDetailState>(OrderDetailState.Uninitialized)

    override fun fetchData(): Job  = viewModelScope.launch{
        orderDetailStateLiveData.value = OrderDetailState.Loading
        orderDetailStateLiveData.value = OrderDetailState.Success(
            orderEntity = orderEntity
        )
    }

     fun reOrder(foodMenuList : List<RestaurantFoodEntity>) = viewModelScope.launch {
         restaurantFoodRepository.clearFoodMenuListInBasket()
         restaurantFoodRepository.insertFoodsMenuInBasket(foodMenuList)
    }
}