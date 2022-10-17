package co.kr.kwon.deliveryapp.screen.review.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.App.Companion.appContext
import co.kr.kwon.deliveryapp.data.repository.gallery.DefaultGalleryPhotoRepository
import co.kr.kwon.deliveryapp.data.repository.gallery.GalleryPhotoRepository
import co.kr.kwon.deliveryapp.model.gallery.GalleryPhotoModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryPhotoRepository: GalleryPhotoRepository
) : BaseViewModel() {

    private lateinit var photoList: MutableList<GalleryPhoto>

    val galleryStateLiveData = MutableLiveData<GalleryState>(GalleryState.Uninitialized)


    override fun fetchData(): Job = viewModelScope.launch {
        galleryStateLiveData.value = GalleryState.Loading

        photoList = galleryPhotoRepository.getAllPhotos()

        galleryStateLiveData.value = GalleryState.Success(photoList = photoList)
    }

    fun selectPhoto(galleryPhoto: GalleryPhoto) {
        val findGalleryPhoto = photoList.find { it.id == galleryPhoto.id }
        findGalleryPhoto?.let { photo ->
            photoList[photoList.indexOf(photo)] =
                photo.copy(
                    isSelected = photo.isSelected.not()
                )
            galleryStateLiveData.value = GalleryState.Success(photoList = photoList)
        }
    }

    fun confirmCheckedPhotos() {

        galleryStateLiveData.value = GalleryState.Loading
        galleryStateLiveData.value = GalleryState.Confirm(
            photoList = photoList.filter { it.isSelected }

        )
    }
}