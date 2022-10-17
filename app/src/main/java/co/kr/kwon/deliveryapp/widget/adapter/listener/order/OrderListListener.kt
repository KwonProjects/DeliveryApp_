package co.kr.kwon.deliveryapp.widget.adapter.listener.order

import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener


interface OrderListListener: AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)
}
