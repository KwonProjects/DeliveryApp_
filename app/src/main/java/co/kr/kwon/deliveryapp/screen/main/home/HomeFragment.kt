package co.kr.kwon.deliveryapp.screen.main.home


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.MapSearchInfoEntity
import co.kr.kwon.deliveryapp.databinding.FragmentHomeBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.screen.base.BaseFragment
import co.kr.kwon.deliveryapp.screen.main.MainActivity
import co.kr.kwon.deliveryapp.screen.main.MainTabMenu
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import co.kr.kwon.deliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantOrder
import co.kr.kwon.deliveryapp.screen.mylocation.MyLocationActivity
import co.kr.kwon.deliveryapp.screen.order.OrderMenuListActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocation ->
                        viewModel.loadReverseGeoInformation(myLocation.locationLatLng)
                    }
            }
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            val responsePermissions = permission.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION) ||
                        (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (responsePermissions.filter { it.value == true }.size == locationPermission.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleTextView) {
                    text = getString(R.string.please_request_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.can_not_assigned_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun initViews() = with(binding) {
        locationTitleTextView.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }
        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone(true)
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible(true)
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipDeliveryTip -> {
                    chipInitialize.isVisible(true)
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible(true)
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)
                }
            }
        }
    }

    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }

    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            orderChipGroup.isVisible(true)
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLng)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
            )
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }
        if (locationLatLng != viewPagerAdapter.locationLatLng) {
            viewPagerAdapter.locationLatLng = locationLatLng
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkMyBasket()
    }

    override fun observeData() {
        with(binding) {
            viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is HomeState.Uninitialized -> {
                        getMyLocation()
                    }
                    is HomeState.Loading -> {
                        locationLoading.isVisible(true)
                        locationTitleTextView.text = getString(R.string.loading)
                    }
                    is HomeState.Success -> {
                        locationLoading.isGone(true)
                        locationTitleTextView.text = it.mapSearchInfo.fullAddress
                        tabLayout.isVisible(true)
                        filterScrollView.isVisible(true)
                        viewPager.isVisible(true)
                        initViewPager(it.mapSearchInfo.locationLatLng)
                        if (it.isLocationSame.not()) {
                            Toast.makeText(requireContext(), R.string.please_request_location_same, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is HomeState.Error -> {
                        locationLoading.isGone(true)
                        locationTitleTextView.setText(R.string.can_not_load_address_info)
                        locationTitleTextView.setOnClickListener {
                            getMyLocation()
                        }
                        Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
                        Log.e("HomeFragment", it.messageId.toString())
                    }
                }
            }

            viewModel.foodMenuBasketLiveData.observe(this@HomeFragment) {
                if (it.isNotEmpty()) {
                    basketButtonContainer.isVisible(true)
                    basketCountTextView.text  = getString(R.string.basket_count,it.size)
                    basketButton.setOnClickListener{
                        if (firebaseAuth.currentUser == null){
                            alertLoginNeed{
                                (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                            }
                        }else{
                            startActivity(
                                OrderMenuListActivity.newIntent(requireContext())
                            )
                        }
                    }
                } else {
                    basketButtonContainer.isGone(true)
                    basketButton.setOnClickListener(null)
                }
            }
        }
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.need_login))
            .setMessage(getString(R.string.go_mytab))
            .setPositiveButton(getString(R.string.confirm_move)) { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            locationPermissionLauncher.launch(locationPermission)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }
    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {

            //binding.locationTitleTextView.text = "${location.latitude} , ${location.longitude}"
            Log.e("MyLocationListener", "${location.latitude} , ${location.longitude}")
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }
    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::locationManager.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    companion object {

        val locationPermission = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }




}