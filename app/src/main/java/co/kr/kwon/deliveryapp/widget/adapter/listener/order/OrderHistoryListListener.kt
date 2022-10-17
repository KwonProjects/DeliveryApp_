package co.kr.kwon.deliveryapp.widget.adapter.listener.order

import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface OrderHistoryListListener : AdapterListener {

    fun reOrder(orderModel: OrderHistoryModel)

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)

    fun orderDetail(orderModel: OrderHistoryModel)

    fun onClickItem(model: OrderHistoryModel)

}