package co.kr.kwon.deliveryapp.data.repository.restaurant

import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>

}
