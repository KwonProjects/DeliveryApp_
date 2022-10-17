package co.kr.kwon.deliveryapp.widget.adapter.viewholder.review

import co.kr.kwon.deliveryapp.databinding.ViewholderRestaurantReviewBinding
import co.kr.kwon.deliveryapp.extensions.clear
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.model.review.RestaurantReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone(true)
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible(true)
                reviewThumbnailImage.load(model.thumbnailImageUri.toString(),24f)
            } else {
                reviewThumbnailImage.isGone(true)
            }
            reviewTitleText.text = model.title
            reviewText.text = model.description

            ratingBar.rating = model.grade.toFloat()
        }
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener)  =  Unit

}