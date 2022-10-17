package co.kr.kwon.deliveryapp.model.gallery

import android.net.Uri
import co.kr.kwon.deliveryapp.data.entity.GalleryPhotoEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantEntity
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model

data class GalleryPhotoModel(
    override val id: Long,
    override val type: CellType = CellType.GALLERY_PHOTO,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    var isSelected: Boolean = false
) : Model(id, type) {

    fun toEntity() = GalleryPhotoEntity(
        id,
        uri,
        name,
        date,
        size,
    )
}