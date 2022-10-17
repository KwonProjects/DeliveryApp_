package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail

import android.content.Context
import android.content.Intent
import android.util.Log
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.databinding.ActivityOrderDetailBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.model.restaurant.order.MenuListModel
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.orderhistory.OrderHistoryListFragment
import co.kr.kwon.deliveryapp.screen.order.OrderMenuListActivity
import co.kr.kwon.deliveryapp.screen.review.AddRestaurantReviewActivity
import co.kr.kwon.deliveryapp.util.date.ConvertDate
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.MenuListAdapter
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : BaseActivity<OrderDetailViewModel, ActivityOrderDetailBinding>() {


    private lateinit var orderEntity: OrderEntity

    override val viewModel by viewModel<OrderDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<OrderEntity>(OrderHistoryListFragment.ORDER_DETAIL_KEY)
        )
    }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val adapter = MenuListAdapter()

    override fun getViewBinding(): ActivityOrderDetailBinding =
        ActivityOrderDetailBinding.inflate(layoutInflater)


    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }

        menuRecyclerView.adapter = adapter

        reorderBtn.setOnClickListener {
            viewModel.reOrder(orderEntity.foodMenuList)
            startActivity(
                OrderMenuListActivity.newIntent(this@OrderDetailActivity)
            )
        }
        writeReview.setOnClickListener {
            startActivity(
                AddRestaurantReviewActivity.newIntent(
                    this@OrderDetailActivity,
                    orderEntity.id,
                    orderEntity.foodMenuList.first().restaurantTitle
                )
            )
        }
    }

    override fun observeData() = viewModel.orderDetailStateLiveData.observe(this) {
        when (it) {
            is OrderDetailState.Loading -> {
                handleLoading()
            }
            is OrderDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }

    }



    private fun handleLoading() = with(binding) {
        progressBar.isVisible(true)
    }

    private fun handleSuccess(state: OrderDetailState.Success) = with(binding) {


        orderEntity = state.orderEntity
        writeReview.isGone(orderEntity.writeReview.not())
        adapter.setPhotoList(orderEntity.foodMenuList)


        Log.e("OrderDetailActivity", "${orderEntity}")
        progressBar.isGone(true)
        writeReview.isGone(orderEntity.writeReview.not())
        Log.e("OrderDetailActivity writeReview.isGone->", orderEntity.writeReview.not().toString())
        val orderEntity = state.orderEntity

        var total_price = 0
        orderEntity.foodMenuList.forEach {
            total_price += it.price
        }

        restaurantTitleText.text = orderEntity.foodMenuList.first().restaurantTitle

        orderNumberData.text = orderEntity.id
        orderDateData.text = ConvertDate(orderEntity.date)

        totalPrice.text = total_price.toString()


    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    companion object {

        const val REORDER_KEY = "REORDER_KEY"

        fun newIntent(context: Context, orderEntity: OrderEntity) =
            Intent(context, OrderDetailActivity::class.java).apply {
                putExtra(OrderHistoryListFragment.ORDER_DETAIL_KEY, orderEntity)
            }
    }
}