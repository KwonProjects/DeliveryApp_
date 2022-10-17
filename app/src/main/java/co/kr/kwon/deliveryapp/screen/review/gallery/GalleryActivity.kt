package co.kr.kwon.deliveryapp.screen.review.gallery

import android.app.Activity
import android.content.Intent
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.ActivityGalleryBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.model.gallery.GalleryPhotoModel
import co.kr.kwon.deliveryapp.model.restaurant.RestaurantModel
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailActivity
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.GalleryPhotoListAdapter
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.gallery.GalleryListListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import co.kr.kwon.deliveryapp.widget.decoration.GridDividerDecoration
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class GalleryActivity : BaseActivity<GalleryViewModel, ActivityGalleryBinding>() {

    override val viewModel by viewModel<GalleryViewModel>()

    override fun getViewBinding(): ActivityGalleryBinding =
        ActivityGalleryBinding.inflate(layoutInflater)

    private val adapter = GalleryPhotoListAdapter {
        viewModel.selectPhoto(it)
    }


    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            GridDividerDecoration(
                this@GalleryActivity,
                R.drawable.bg_frame_gallery
            )
        )
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhotos()
        }
    }


    override fun observeData() = viewModel.galleryStateLiveData.observe(this) {

        when (it) {
            is GalleryState.Loading -> handleLoading()
            is GalleryState.Success -> handleSuccess(it)
            is GalleryState.Confirm -> handleConfirm(it)
            else -> Unit
        }

    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible(true)
        recyclerView.isGone(true)
    }

    private fun handleConfirm(state: GalleryState.Confirm) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
        })
        finish()
    }

    private fun handleSuccess(state: GalleryState.Success) = with(binding) {
        progressBar.isGone(true)
        recyclerView.isVisible(true)
        adapter.setPhotoList(state.photoList)
    }

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, GalleryActivity::class.java)

        const val URI_LIST_KEY = "uriList"
    }
}