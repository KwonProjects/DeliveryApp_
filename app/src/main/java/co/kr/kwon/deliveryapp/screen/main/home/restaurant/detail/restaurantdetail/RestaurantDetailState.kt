package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail

import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity

sealed class RestaurantDetailState {

    object Uninitialized : RestaurantDetailState()

    object Loading : RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList: List<RestaurantFoodEntity>? = null,
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction: Pair<Boolean, () -> Unit> = Pair(false, {}),
        val isLiked: Boolean? = null
    ) : RestaurantDetailState()

}