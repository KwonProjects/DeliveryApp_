package co.kr.kwon.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderMenuListEntity(
    val date : String,
    val id: String,
    val foodMenuList : RestaurantFoodEntity
) : Parcelable