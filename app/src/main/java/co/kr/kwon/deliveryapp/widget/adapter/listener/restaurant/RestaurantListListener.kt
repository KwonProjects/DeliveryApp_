package co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant

import co.kr.kwon.deliveryapp.model.restaurant.RestaurantModel
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface RestaurantListListener   : AdapterListener{

    fun onClickItem(model: RestaurantModel)
}