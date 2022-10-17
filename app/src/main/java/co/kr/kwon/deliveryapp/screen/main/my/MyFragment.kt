package co.kr.kwon.deliveryapp.screen.main.my

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.FragmentMyBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.model.review.UnWrittenReviewModel
import co.kr.kwon.deliveryapp.screen.base.BaseFragment
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login.LoginActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.logout.MyInfoActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail.OrderDetailActivity
import co.kr.kwon.deliveryapp.screen.main.orderhistory.OrderHistoryListViewModel
import co.kr.kwon.deliveryapp.screen.review.AddRestaurantReviewActivity
import co.kr.kwon.deliveryapp.util.event.MenuChangeEventBus
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.adapter.ModelRecyclerAdapter
import co.kr.kwon.deliveryapp.widget.adapter.listener.AdapterListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.order.OrderHistoryListListener
import co.kr.kwon.deliveryapp.widget.adapter.listener.review.WriteReviewListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val resourcesProvider by inject<ResourcesProvider>()


    private val adapter by lazy {
        ModelRecyclerAdapter<UnWrittenReviewModel, MyViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object : WriteReviewListener {
                override fun onClick(item: UnWrittenReviewModel) {
                    startActivity(
                        AddRestaurantReviewActivity.newIntent(
                            requireContext(),
                            item.orderId,
                            item.foodMenuList.first().restaurantTitle
                        )
                    )
                }

            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        if (firebaseAuth.currentUser?.uid != null) {
            orderBottom.isGone(true)
            viewModel.setUserInfo(firebaseAuth.currentUser)
        } else {
            orderBottom.isVisible(true)
        }


        loginBtn.setOnClickListener {
            startActivity(
                LoginActivity.newIntent(requireContext())
            )
        }
        myInfo.setOnClickListener {
            startActivity(
                MyInfoActivity.newIntent(requireContext())
            )
        }
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    override fun observeData() = viewModel.myStateLiveData.observe(this) {
        when (it) {
            is MyState.Uninitialized -> initViews()
            is MyState.Loading -> handleLoadingState()
            is MyState.Success -> handleSuccessState(it)
            is MyState.Error -> handleErrorState(it)
            else -> {}
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible(true)
        loginRequiredGroup.isGone(true)
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone(true)
        when (state) {
            is MyState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone(true)
                loginRequiredGroup.isVisible(true)
            }
            else -> {}
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {

        profileGroup.isVisible(true)
        loginRequiredGroup.isGone(true)
        profileImageView.load(state.profileImageUri.toString(), 60f)
        Log.e("MyFragment", "ProfileImage result : " + state.profileImageUri)
        userNameTextView.text = state.userName
        userEmail.text = state.userEmail

        //adapter.submitList(state.reviewList)
    }

    private fun handleErrorState(state: MyState.Error) {
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }


    companion object {

        fun newInstance() = MyFragment()
        const val TAG = "MyFragment"
    }
}