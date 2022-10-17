package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.databinding.FragmentListBinding
import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.screen.base.BaseFragment
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailViewModel
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment :
    BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantMenuListViewModel> {
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }
    private val restaurantFoodList by lazy {
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(
            FOOD_LIST_KEY
        )
    }

    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : FoodMenuListListener {
                override fun onClickItem(item: FoodModel) {
                    viewModel.insertMenuInBasket(item)
                }

            }
        )
    }

    override fun initViews() {
        with(binding) {
            recyclerView.adapter = adapter
        }
    }

    override fun observeData() {
        viewModel.restaurantFoodListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.menuBasketLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "장바구니에 담겼습니. 메뉴 : ${it.title}", Toast.LENGTH_SHORT).show()
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }
        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner){ (isClearNeed,afterAction) ->
            if (isClearNeed){
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed,afterAction)
            }

        }
    }

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"

        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(
            restaurantId: Long,
            foodList: ArrayList<RestaurantFoodEntity>
        ): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}