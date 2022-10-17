package co.kr.kwon.deliveryapp.screen.main.orderhistory

import androidx.annotation.StringRes
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel


sealed class OrderHistoryListState {

    object Uninitialized : OrderHistoryListState()

    object Loading : OrderHistoryListState()

    data class Login(
        val idToken: String
    ): OrderHistoryListState()

    sealed class Success : OrderHistoryListState() {

        data class Registered(
            val orderList: List<OrderHistoryModel>,
        ) : Success()

        object NotRegistered : Success()

    }


    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ) : OrderHistoryListState()
}