package co.kr.kwon.deliveryapp.widget.adapter.listener.order

import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {

    fun onRemoveItem(model: FoodModel)
}
