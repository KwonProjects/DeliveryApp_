package co.kr.kwon.deliveryapp.screen.order

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.databinding.ActivityOrderMenuListBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.model.restaurant.food.FoodModel
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail.OrderDetailActivity
import co.kr.kwon.deliveryapp.screen.main.orderhistory.OrderHistoryListFragment
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override val viewModel by viewModel<OrderMenuListViewModel>()

    override fun getViewBinding(): ActivityOrderMenuListBinding =
        ActivityOrderMenuListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(model: FoodModel) {
                    viewModel.removeOrderMenu(model)
                }
            })
    }

    override fun initViews() = with(binding) {

        recyclerView.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmBtn.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearBtn.setOnClickListener {
            alertAllClearMenu {
                viewModel.clearOrderMenu()

            }
        }
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this) {
        when (it) {
            is OrderMenuState.Loading -> {
                handleLoading()
            }
            is OrderMenuState.Success -> {
                handleSuccess(it)
            }
            is OrderMenuState.Order -> {
                handleOrderState()
            }
            is OrderMenuState.Error -> {
                handleErrorState(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible(true)
    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone(true)
        adapter.submitList(state.restaurantFoodModelList) /** basketFoodList **/
        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmBtn.isEnabled = menuOrderIsEmpty.not()
        if (menuOrderIsEmpty) {
            Toast.makeText(
                this@OrderMenuListActivity,
                getString(R.string.empty_order),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun handleOrderState() {
        Toast.makeText(
            this@OrderMenuListActivity,
            getString(R.string.success_order),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun handleErrorState(state: OrderMenuState.Error) {
        Toast.makeText(
            this@OrderMenuListActivity,
            getString(state.messageId, state.e),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun alertAllClearMenu(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_remove_allmenu))
            .setMessage(getString(R.string.content_all_menu))
            .setPositiveButton(getString(R.string.dialog_yes)) { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)

//        fun newIntent(context: Context, orderEntity: OrderEntity) =
//            Intent(context, OrderMenuListActivity::class.java).apply {
//                putExtra(OrderDetailActivity.REORDER_KEY, orderEntity)
//            }
    }
}