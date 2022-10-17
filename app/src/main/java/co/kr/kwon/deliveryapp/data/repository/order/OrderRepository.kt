package co.kr.kwon.deliveryapp.data.repository.order

import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        date : Long,
        userId : String,
        restaurantId : Long,
        foodMenuList : List<RestaurantFoodEntity>
    ) : Result

    suspend fun getAllOrderMenus(
        userId: String
    ) : Result
}
