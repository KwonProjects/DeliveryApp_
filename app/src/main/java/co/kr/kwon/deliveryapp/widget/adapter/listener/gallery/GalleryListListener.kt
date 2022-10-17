package co.kr.kwon.deliveryapp.widget.adapter.listener.gallery

import co.kr.kwon.deliveryapp.model.gallery.GalleryPhotoModel
import co.kr.kwon.deliveryapp.screen.review.gallery.GalleryPhoto
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

interface GalleryListListener : AdapterListener {

    fun onSelectPhoto(galleryPhoto: GalleryPhoto)
}