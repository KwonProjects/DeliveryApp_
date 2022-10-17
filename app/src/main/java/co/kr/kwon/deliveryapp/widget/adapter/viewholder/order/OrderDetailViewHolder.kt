package co.kr.kwon.deliveryapp.widget.adapter.viewholder.order

import co.kr.kwon.deliveryapp.databinding.ViewholderMenuListBinding
import co.kr.kwon.deliveryapp.databinding.ViewholderOrderMenuBinding
import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderDetailViewHolder(
    private val binding: ViewholderMenuListBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderHistoryModel>(binding, viewModel, resourcesProvider) {
    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun bindData(model: OrderHistoryModel) = with(binding) {
        orderMenu.text = model.foodMenuList.first().title
        orderMenu.text = model.foodMenuList.first().price.toString()
    }

    override fun bindViews(model: OrderHistoryModel, adapterListener: AdapterListener)  = Unit


}