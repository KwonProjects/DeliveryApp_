package co.kr.kwon.deliveryapp.screen.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.repository.food.RestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.order.OrderRepository
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository : OrderRepository
) : BaseViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    val orderMenuStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)

    override fun fetchData(): Job  = viewModelScope.launch{
        orderMenuStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        orderMenuStateLiveData.value  = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    restaurantTitle = it.restaurantTitle,
                    restaurantImageUrl = it.restaurantImageUrl,
                    foodId = it.id
                )
            }
        )
    }

    fun clearOrderMenu()  = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(model: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuListInBasket(model.foodId)
        fetchData()
    }

    fun orderMenu()  = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        if (foodMenuList.isNotEmpty()){
            val restaurantId = foodMenuList.first().restaurantId
            firebaseAuth.currentUser?.let { user ->
                val currentTime = System.currentTimeMillis()
                when(val data =  orderRepository.orderMenu(currentTime,user.uid,restaurantId,foodMenuList)){
                    is Result.Success<*> ->{
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        orderMenuStateLiveData.value =  OrderMenuState.Order
                    }
                    is Result.Error ->{
                        orderMenuStateLiveData.value = OrderMenuState.Error(
                            R.string.request_error, data.e
                        )
                    }
                }
            } ?: kotlin.run {
                orderMenuStateLiveData.value  = OrderMenuState.Error(
                    R.string.user_id_not_found,IllegalStateException()
                )
            }
        }
    }
}