package co.kr.kwon.deliveryapp.widget.adapter.viewholder

import co.kr.kwon.deliveryapp.databinding.ViewholderEmptyBinding
import co.kr.kwon.deliveryapp.model.Model
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}