package co.kr.kwon.deliveryapp.widget.adapter.viewholder.orderhistorylist


import android.util.Log
import co.kr.kwon.deliveryapp.databinding.ViewholderOrderHistoryListBinding
import co.kr.kwon.deliveryapp.extensions.clear
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.date.ConvertDate
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.order.OrderHistoryListListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderHistoryListViewHolder(
    private val binding: ViewholderOrderHistoryListBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderHistoryModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
        orderContentText.text = ""
    }

    override fun bindData(model: OrderHistoryModel) {
        super.bindData(model)
        with(binding) {
            val foodMenuList = model.foodMenuList
            orderDate.text = ConvertDate(model.date)
            restaurantImage.load(foodMenuList.first().restaurantImageUrl)
            restaurantTitleText.text = foodMenuList.first().restaurantTitle

            foodMenuList
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                    val orderDataStr =
                        orderContentText.text.toString() + "메뉴 : $title X ${menuList.size}\n"

                    orderContentText.text = orderDataStr
                    Log.e("OrderHistoryListViewHolder", orderDataStr)
                }
            orderContentText.text = orderContentText.text.trim()
            reviewBtn.isGone(model.writeReview.not())

        }
    }

    override fun bindViews(model: OrderHistoryModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is OrderHistoryListListener) {
                reorderBtn.setOnClickListener {
                    adapterListener.reOrder(model)
                }

                reviewBtn.setOnClickListener {
                    adapterListener.writeRestaurantReview(
                        model.orderId,
                        model.foodMenuList.first().restaurantTitle
                    )
                }

                orderDetail.setOnClickListener {
                    adapterListener.orderDetail(model)
                }
                root.setOnClickListener {
                    adapterListener.onClickItem(model)
                }
            }


        }
}