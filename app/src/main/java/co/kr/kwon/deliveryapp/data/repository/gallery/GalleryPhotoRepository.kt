package co.kr.kwon.deliveryapp.data.repository.gallery

import co.kr.kwon.deliveryapp.model.gallery.GalleryPhotoModel
import co.kr.kwon.deliveryapp.screen.review.gallery.GalleryPhoto

interface GalleryPhotoRepository{

    suspend fun getAllPhotos(): MutableList<GalleryPhoto>
}
