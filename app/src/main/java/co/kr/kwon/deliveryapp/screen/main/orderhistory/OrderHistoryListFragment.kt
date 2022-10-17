package co.kr.kwon.deliveryapp.screen.main.orderhistory

import android.util.Log
import android.widget.Toast
import co.kr.kwon.deliveryapp.databinding.FragmentOrderHistoryListBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderModel
import co.kr.kwon.deliveryapp.screen.base.BaseFragment
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail.OrderDetailActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailActivity
import co.kr.kwon.deliveryapp.screen.order.OrderMenuListActivity
import co.kr.kwon.deliveryapp.screen.review.AddRestaurantReviewActivity
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.order.OrderHistoryListListener
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OrderHistoryListFragment :
    BaseFragment<OrderHistoryListViewModel, FragmentOrderHistoryListBinding>() {

    override val viewModel by viewModel<OrderHistoryListViewModel>()

    override fun getViewBinding(): FragmentOrderHistoryListBinding =
        FragmentOrderHistoryListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var isFirstShown = false

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderHistoryModel, OrderHistoryListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object :
                OrderHistoryListListener {
                override fun reOrder(orderModel: OrderHistoryModel) {
                    viewModel.reOrder(orderModel.foodMenuList)
                    startActivity(
                        OrderMenuListActivity.newIntent(requireContext())
                    )

                }

                override fun writeRestaurantReview(orderId: String, restaurantTitle: String) {
                    startActivity(
                        AddRestaurantReviewActivity.newIntent(
                            requireContext(),
                            orderId,
                            restaurantTitle
                        )
                    )
                }

                override fun orderDetail(orderHistoryModel: OrderHistoryModel) {
                    startActivity(
                        OrderDetailActivity.newIntent(
                            requireContext(),
                            orderHistoryModel.toEntity()
                        )
                    )
                }

                override fun onClickItem(model: OrderHistoryModel) {

                }

            })
    }


    override fun initViews() = with(binding) {

        recyclerView.adapter = adapter

    }


    override fun observeData() = viewModel.orderHistoryListLiveData.observe(this) {
        when (it) {
            is OrderHistoryListState.Uninitialized -> initViews()
            is OrderHistoryListState.Loading -> handleLoadingState()
            is OrderHistoryListState.Login -> handleLoginState(it)
            is OrderHistoryListState.Success -> handleSuccessState(it)
            is OrderHistoryListState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible(true)
        recyclerView.isGone(true)
    }

    private fun handleLoginState(state: OrderHistoryListState.Login) = with(binding) {
        progressBar.isVisible(true)
        if (firebaseAuth.currentUser != null && state.idToken.equals(firebaseAuth.currentUser)) {
            viewModel.setUserInfo(firebaseAuth)
        } else {
            viewModel.setUserInfo(null)
        }
    }

    private fun handleSuccessState(state: OrderHistoryListState.Success) = with(binding) {
        progressBar.isGone(true)
        when (state) {
            is OrderHistoryListState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is OrderHistoryListState.Success.NotRegistered -> {
                recyclerView.isGone(true)
            }
        }
    }

    private fun handleRegisteredState(state: OrderHistoryListState.Success.Registered) =
        with(binding) {
            orderBottom.isGone(true)
            recyclerView.isVisible(true)
            if (state.orderList.isEmpty()){
                recyclerView.isGone(true)
                emptyResultTextView.isVisible(true)
            }else{
                emptyResultTextView.isGone(true)
                adapter.submitList(state.orderList)
            }

        }

    private fun handleErrorState(state: OrderHistoryListState.Error) {
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()

        binding.recyclerView.adapter = adapter

        viewModel.setUserInfo(firebaseAuth)

    }

    companion object {
        const val ORDER_DETAIL_KEY = "OrderDetail"

        fun newInstance() = OrderHistoryListFragment()
        const val TAG = "OrderHistoryListFragment"
    }

}