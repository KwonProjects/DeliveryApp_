package co.kr.kwon.deliveryapp.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import co.kr.kwon.deliveryapp.util.convertor.RoomTypeConverters
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize


@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?,
    val lat : Double,
    val lng : Double
): Entity, Parcelable