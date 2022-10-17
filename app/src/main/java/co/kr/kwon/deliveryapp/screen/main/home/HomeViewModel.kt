package co.kr.kwon.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.entity.MapSearchInfoEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.data.repository.food.RestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.map.MapRepository
import co.kr.kwon.deliveryapp.data.repository.user.UserRepository
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()


    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) =
        viewModelScope.launch {
            homeStateLiveData.value = HomeState.Loading
            val userLocation = userRepository.getUserLocation()
            val currentLocation = userLocation ?: locationLatLngEntity

            val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
            addressInfo?.let { info ->
                homeStateLiveData.value = HomeState.Success(
                    MapSearchInfoEntity(
                        fullAddress = info.fullAddress ?: "주소 정보 없음",
                        name = info.buildingName ?: "빌딩정보 없음",
                        locationLatLng = currentLocation
                    ),
                    isLocationSame = userLocation == locationLatLngEntity
                )
            } ?: kotlin.run {
                homeStateLiveData.value = HomeState.Error(
                    R.string.can_not_load_address_info
                )
            }
        }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfo
            }
            else -> Unit
        }
        return null
    }

    fun checkMyBasket()  = viewModelScope.launch{
        foodMenuBasketLiveData.value = restaurantFoodRepository.getAllFoodMenuListInBasket()
    }

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }
}