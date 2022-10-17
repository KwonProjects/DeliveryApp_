package co.kr.kwon.deliveryapp.widget.adapter.viewholder.food

import android.util.Log
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.ViewholderFoodMenuBinding
import co.kr.kwon.deliveryapp.extensions.clear
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import co.kr.kwon.deliveryapp.widget.adapter.viewholder.ModelViewHolder
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class FoodMenuViewHolder(
    private val binding: ViewholderFoodMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        Log.e("FoodMenuViewHolder", "foodImage url : ${model.imageUrl}")
        with(binding) {
            foodImage.load(model.imageUrl, 24f, CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price, model.price)

        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener)  = with(binding){
        if (adapterListener is FoodMenuListListener){
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}