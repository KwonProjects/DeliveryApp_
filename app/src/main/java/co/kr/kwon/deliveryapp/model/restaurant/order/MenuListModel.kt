package co.kr.kwon.deliveryapp.model.restaurant.order

import co.kr.kwon.deliveryapp.data.entity.OrderMenuListEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model

data class MenuListModel(
    val date : String,
    override val id: Long,
    override val type: CellType = CellType.MENU_LIST_CELL,
    val orderId: String,
    val foodMenuList : RestaurantFoodEntity
    ) : Model(id, type){
        fun toEntity() =  OrderMenuListEntity(
            date = date,
            id = orderId,
            foodMenuList = foodMenuList
        )
    }

