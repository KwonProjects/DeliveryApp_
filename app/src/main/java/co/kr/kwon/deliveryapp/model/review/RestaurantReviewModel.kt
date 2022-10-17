package co.kr.kwon.deliveryapp.model.review

import android.net.Uri
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Float,
    val thumbnailImageUri: Uri? = null
) : Model(id, type)
