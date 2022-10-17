package co.kr.kwon.deliveryapp.model.restaurant.order

import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model


data class OrderHistoryModel(
    val date  :Long,
    override val id: Long,
    override val type: CellType = CellType.ORDER_HISTORY_LIST_CELL,
    val orderId: String,
    val userId : String,
    val restaurantId :Long,
    val foodMenuList : List<RestaurantFoodEntity>,
    val writeReview : Boolean
) : Model(id, type){
    fun toEntity() =  OrderEntity(
        date = date,
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList,
        writeReview = writeReview
    )
}