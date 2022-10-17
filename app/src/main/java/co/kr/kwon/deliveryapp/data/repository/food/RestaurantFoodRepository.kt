package co.kr.kwon.deliveryapp.data.repository.food

import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(
        restaurantId: Long,
        restaurantTitle: String,
        restaurantImageUrl: String
    ): List<RestaurantFoodEntity>

    suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity>

    suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity>

    suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity)

    suspend fun insertFoodsMenuInBasket(restaurantFoodsEntity: List<RestaurantFoodEntity>)

    suspend fun removeFoodMenuListInBasket(foodId: String)

    suspend fun clearFoodMenuListInBasket()



}
