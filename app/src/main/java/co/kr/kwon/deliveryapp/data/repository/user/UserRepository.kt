package co.kr.kwon.deliveryapp.data.repository.user

import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun getUserLocation() : LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    suspend fun getUserInfo(firebaseUser: FirebaseUser)

    suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity?

    suspend fun getAllUserLikedRestaurantList() : List<RestaurantEntity>

    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteAllUserLikedRestaurant()

}