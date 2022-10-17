package co.kr.kwon.deliveryapp.model.review

import android.net.Uri
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model

data class UnWrittenReviewModel(
    val date  :Long,
    override val id: Long,
    override val type: CellType = CellType.UNWRITTEN_REVIEW_CELL,
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
