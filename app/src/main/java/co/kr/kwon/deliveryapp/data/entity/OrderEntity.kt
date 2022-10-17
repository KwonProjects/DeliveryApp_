package co.kr.kwon.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OrderEntity(
    val date : Long,
    val id: String,
    val userId  : String,
    val restaurantId : Long,
    val foodMenuList : List<RestaurantFoodEntity>,
    val writeReview : Boolean
) : Parcelable