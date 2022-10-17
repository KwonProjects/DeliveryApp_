package co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant

import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface FoodMenuListListener : AdapterListener {

    fun onClickItem(item : FoodModel)
}