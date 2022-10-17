package co.kr.kwon.deliveryapp.data.repository.map

import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import co.kr.kwon.deliveryapp.data.network.MapApiService
import co.kr.kwon.deliveryapp.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository {

    override suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo? = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )
        if (response.isSuccessful){
            response?.body()?.addressInfo
        }else{
            null
        }
    }
}
