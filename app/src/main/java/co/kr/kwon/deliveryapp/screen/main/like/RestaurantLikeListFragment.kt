package co.kr.kwon.deliveryapp.screen.main.like

import co.kr.kwon.deliveryapp.databinding.FragmentRestaurantLikeListBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.model.restaurant.RestaurantModel
import co.kr.kwon.deliveryapp.screen.base.BaseFragment
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailActivity
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment: BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override fun getViewBinding(): FragmentRestaurantLikeListBinding = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    private var isFirstShown = false

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(listOf(), viewModel,resourcesProvider ,adapterListener = object :
            RestaurantLikeListListener {

            override fun onDislikeItem(model: RestaurantModel) {
                viewModel.dislikeRestaurant(model.toEntity())
            }

            override fun onClickItem(model: RestaurantModel) {
                startActivity(
                    RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstShown.not()) {
            isFirstShown = true
        } else {
            viewModel.fetchData()
        }
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner){
        checkListEmpty(it)
    }


    private fun checkListEmpty(restaurantList: List<RestaurantModel>)  = with(binding){
        val isEmpty = restaurantList.isEmpty()
        recyclerView.isGone(isEmpty)
        emptyResultTextView.isVisible(isEmpty)
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {

        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "likeFragment"

    }

}
