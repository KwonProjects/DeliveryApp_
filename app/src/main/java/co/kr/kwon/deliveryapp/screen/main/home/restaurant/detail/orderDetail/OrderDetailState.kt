package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail

import androidx.annotation.StringRes
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.model.restaurant.order.MenuListModel


sealed class OrderDetailState {

    object Uninitialized : OrderDetailState()

    object Loading : OrderDetailState()

    data class Success(
        val orderEntity: OrderEntity
    ) : OrderDetailState()

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ):OrderDetailState()

}