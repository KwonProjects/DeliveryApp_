package co.kr.kwon.deliveryapp.data.repository.user

import co.kr.kwon.deliveryapp.data.db.dao.LocationDao
import co.kr.kwon.deliveryapp.data.db.dao.RestaurantDao
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val restaurantDao: RestaurantDao,
    private val firebaseAuth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(dispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)  = withContext(dispatcher){
        locationDao.insert(locationLatLngEntity)
    }

    override suspend fun getUserInfo(firebaseUser: FirebaseUser) {

    }

    override suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity? = withContext(dispatcher){
        restaurantDao.get(restaurantTitle)
    }

    override suspend fun getAllUserLikedRestaurantList(): List<RestaurantEntity> = withContext(dispatcher) {
        restaurantDao.getAll()
    }

    override suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity) = withContext(dispatcher){
        restaurantDao.insert(restaurantEntity)
    }

    override suspend fun deleteUserLikedRestaurant(restaurantTitle: String) {
        restaurantDao.delete(restaurantTitle)
    }

    override suspend fun deleteAllUserLikedRestaurant() {
        restaurantDao.deleteAll()
    }
}