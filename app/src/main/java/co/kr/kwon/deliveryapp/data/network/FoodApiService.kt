package co.kr.kwon.deliveryapp.data.network

import co.kr.kwon.deliveryapp.data.response.restaurant.RestaurantFoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {

    @GET("restaurants/{restaurantId}/foods")
    suspend fun getRestaurantFoods(
        @Path("restaurantId") restaurantId : Long
    ) : Response<List<RestaurantFoodResponse>>
}
