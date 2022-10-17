package co.kr.kwon.deliveryapp.screen.review.gallery

import co.kr.kwon.deliveryapp.model.gallery.GalleryPhotoModel

sealed class GalleryState {

    object Uninitialized: GalleryState()

    object Loading: GalleryState()

    data class Success(
        val photoList: MutableList<GalleryPhoto>
    ): GalleryState()

    data class Confirm(
        val photoList: List<GalleryPhoto>
    ): GalleryState()

}
