package co.kr.kwon.deliveryapp.widget.adapter.viewholder.review

import co.kr.kwon.deliveryapp.databinding.ViewholderLikeRestaurantBinding
import co.kr.kwon.deliveryapp.databinding.ViewholderUnwrittenReviewBinding
import co.kr.kwon.deliveryapp.extensions.clear
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.date.ConvertDate
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.review.WriteReviewListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class UnWrittenReviewListViewHolder(
    private val binding: ViewholderUnwrittenReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<UnWrittenReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: UnWrittenReviewModel) = with(binding) {
        restaurantTitleText.text = model.foodMenuList.first().restaurantTitle
        orderDate.text = ConvertDate(model.date)
        var menuList = ""
        for (i in 0..model.foodMenuList.size) {
            if (model.foodMenuList.size >= 2) {
                menuList = "${model.foodMenuList.first().title} 외 ${model.foodMenuList.size - 1}개"
            } else {
                menuList = "${model.foodMenuList.first().title}"
            }
        }
        binding.menuList.text = menuList
        binding.restaurantImage.load(model.foodMenuList.first().imageUrl, 8f)
    }

    override fun bindViews(model: UnWrittenReviewModel, adapterListener: AdapterListener) {
        with(binding) {
            if (adapterListener is WriteReviewListener) {
                reviewBtn.setOnClickListener {
                    adapterListener.onClick(model)
                }
            }
        }
    }

}
