package co.kr.kwon.deliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import co.kr.kwon.deliveryapp.databinding.*
import co.kr.kwon.deliveryapp.model.CellType
import co.kr.kwon.deliveryapp.model.Model
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.EmptyViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.food.FoodMenuViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.order.OrderMenuListViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.order.OrderMenuViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.orderhistorylist.OrderHistoryListViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.review.RestaurantReviewViewHolder
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.review.UnWrittenReviewListViewHolder


object ModelViewHolderMapper {
    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> RestaurantReviewViewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderHistoryListViewHolder(
                ViewholderOrderHistoryListBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_HISTORY_LIST_CELL -> OrderHistoryListViewHolder(
                ViewholderOrderHistoryListBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.MENU_LIST_CELL -> OrderMenuListViewHolder(
                ViewholderMenuListBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            CellType.UNWRITTEN_REVIEW_CELL -> UnWrittenReviewListViewHolder(
                ViewholderUnwrittenReviewBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            else -> Unit
        }
        return viewHolder as ModelViewHolder<M>
    }
}
