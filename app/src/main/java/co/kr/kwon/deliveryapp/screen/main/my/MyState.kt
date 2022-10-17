package co.kr.kwon.deliveryapp.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel
import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel

sealed class MyState {

    object Uninitialized : MyState()

    object Loading : MyState()

    data class Login(
        val idToken: String
    ) : MyState()

    sealed class Success : MyState() {

        data class Registered(
            val userName: String,
            val userEmail: String,
            val profileImageUri: Uri?,
        ) : Success()

        data class checkReview(
            val userName: String,
            val userEmail: String,
            val profileImageUri: Uri?,
            val reviewList: List<UnWrittenReviewModel>
        ) : Success()

        object NotRegistered : Success()

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ) : MyState()

}
