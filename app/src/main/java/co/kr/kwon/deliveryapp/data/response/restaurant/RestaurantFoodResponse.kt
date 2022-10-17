package co.kr.kwon.deliveryapp.data.response.restaurant

import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String
){

    fun toEntity(restaurantId: Long,restaurantTitle : String,restaurantImageUrl : String) = RestaurantFoodEntity(
        id,
        title,
        description,
        price.toDouble().toInt(),
        imageUrl,
        restaurantId,
        restaurantTitle,
        restaurantImageUrl
    )
}
