package co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant

import co.kr.kwon.deliveryapp.model.restaurant.RestaurantModel


interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}
