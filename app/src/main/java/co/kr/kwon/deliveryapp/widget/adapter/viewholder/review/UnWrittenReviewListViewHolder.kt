package co.kr.kwon.deliveryapp.widget.adapter.viewholder.review

import co.kr.kwon.deliveryapp.databinding.ViewholderLikeRestaurantBinding
import co.kr.kwon.deliveryapp.databinding.ViewholderUnwrittenReviewBinding
import co.kr.kwon.deliveryapp.extensions.clear
import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.review.WriteReviewListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class UnWrittenReviewListViewHolder(
    private val binding: ViewholderUnwrittenReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<UnWrittenReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: UnWrittenReviewModel)  = with(binding){
        restaurantTitleText.text = model.foodMenuList.first().restaurantTitle
    }

    override fun bindViews(model: UnWrittenReviewModel, adapterListener: AdapterListener) {
        with(binding) {
            if (adapterListener is WriteReviewListener) {
                writeReview.setOnClickListener {
                    adapterListener.onClick(model)
                }
            }
        }
    }

}
