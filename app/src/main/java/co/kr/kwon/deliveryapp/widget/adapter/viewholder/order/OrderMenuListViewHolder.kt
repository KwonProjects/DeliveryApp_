package co.kr.kwon.deliveryapp.widget.adapter.viewholder.order

import co.kr.kwon.deliveryapp.databinding.ViewholderMenuListBinding
import co.kr.kwon.deliveryapp.model.restaurant.order.MenuListModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder


class OrderMenuListViewHolder(
    private val binding: ViewholderMenuListBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<MenuListModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        orderMenu.text = ""
        priceText.text = ""
    }

    override fun bindData(model: MenuListModel) {
        super.bindData(model)
        with(binding) {
            orderMenu.text = model.foodMenuList.title
            priceText.text = model.foodMenuList.price.toString()
        }
    }

    override fun bindViews(model: MenuListModel, adapterListener: AdapterListener)  = Unit

}
