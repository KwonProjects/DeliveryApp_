package co.kr.kwon.deliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.kwon.deliveryapp.data.db.dao.FoodMenuBasketDao
import co.kr.kwon.deliveryapp.data.db.dao.LocationDao
import co.kr.kwon.deliveryapp.data.db.dao.RestaurantDao
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class , RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        const val DATABASE_NAME = "DeliveryApp.db"
    }

    abstract fun LocationDao() : LocationDao

    abstract fun RestaurantDao() : RestaurantDao

    abstract fun FoodMenuBasketDao() : FoodMenuBasketDao
}