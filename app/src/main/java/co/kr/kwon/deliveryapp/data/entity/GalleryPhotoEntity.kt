package co.kr.kwon.deliveryapp.data.entity

import android.net.Uri


data class GalleryPhotoEntity(
    override val id: Long,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    var isSelected: Boolean = false
) : Entity