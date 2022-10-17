package co.kr.kwon.deliveryapp.screen.review

import androidx.annotation.StringRes
import co.kr.kwon.deliveryapp.data.entity.MapSearchInfoEntity

sealed class AddRestaurantReviewState{

    object Uninitialized : AddRestaurantReviewState()

    object Loading : AddRestaurantReviewState()

    data class  Success(
        val IsWrite: Boolean
    ) : AddRestaurantReviewState()

    object Review : AddRestaurantReviewState()

    data class Error(
        @StringRes val messageId  : Int,
        val e : Throwable
    ) : AddRestaurantReviewState()
}