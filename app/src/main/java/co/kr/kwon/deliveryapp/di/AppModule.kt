package co.kr.kwon.deliveryapp.di

import co.kr.kwon.deliveryapp.data.entity.*
import co.kr.kwon.deliveryapp.data.preference.AppPreferenceManager
import co.kr.kwon.deliveryapp.data.repository.food.DefaultRestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.food.RestaurantFoodRepository
import co.kr.kwon.deliveryapp.data.repository.gallery.DefaultGalleryPhotoRepository
import co.kr.kwon.deliveryapp.data.repository.gallery.GalleryPhotoRepository
import co.kr.kwon.deliveryapp.data.repository.map.DefaultMapRepository
import co.kr.kwon.deliveryapp.data.repository.map.MapRepository
import co.kr.kwon.deliveryapp.data.repository.order.DefaultOrderRepository
import co.kr.kwon.deliveryapp.data.repository.order.OrderRepository
import co.kr.kwon.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import co.kr.kwon.deliveryapp.data.repository.restaurant.RestaurantRepository
import co.kr.kwon.deliveryapp.data.repository.review.DefaultRestaurantReviewRepository
import co.kr.kwon.deliveryapp.data.repository.review.RestaurantReviewRepository
import co.kr.kwon.deliveryapp.data.repository.user.DefaultUserRepository
import co.kr.kwon.deliveryapp.data.repository.user.UserRepository
import co.kr.kwon.deliveryapp.screen.main.MainViewModel
import co.kr.kwon.deliveryapp.screen.main.home.HomeViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login.LoginViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.logout.MyInfoViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.restaurantdetail.RestaurantDetailViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.orderDetail.OrderDetailViewModel
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import co.kr.kwon.deliveryapp.screen.main.like.RestaurantLikeListViewModel
import co.kr.kwon.deliveryapp.screen.main.my.MyViewModel
import co.kr.kwon.deliveryapp.screen.main.orderhistory.OrderHistoryListViewModel
import co.kr.kwon.deliveryapp.screen.mylocation.MyLocationViewModel
import co.kr.kwon.deliveryapp.screen.order.OrderMenuListViewModel
import co.kr.kwon.deliveryapp.screen.review.AddRestaurantReviewViewModel
import co.kr.kwon.deliveryapp.screen.review.gallery.GalleryViewModel
import co.kr.kwon.deliveryapp.util.event.MenuChangeEventBus
import co.kr.kwon.deliveryapp.util.provider.DefaultResourcesProvider
import co.kr.kwon.deliveryapp.util.provider.ResourcesProvider
import co.kr.kwon.deliveryapp.widget.dialog.BottomSheetViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get()) }

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity)
        ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity, get(), get())
    }
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(
            restaurantEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get())
    }

    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { OrderMenuListViewModel(get(), get()) }
    viewModel { OrderHistoryListViewModel(get(), get(),get()) }
    viewModel {(orderEntity : OrderEntity) ->
        OrderDetailViewModel(
            orderEntity,get(),get()
        )
    }
    viewModel { MainViewModel() }
    viewModel { (orderId : String) ->
        AddRestaurantReviewViewModel(orderId,get()) }
    viewModel { GalleryViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MyInfoViewModel() }


    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get(),get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(),get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }
    single<GalleryPhotoRepository> { DefaultGalleryPhotoRepository(get(), get()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }

    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidContext()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

    single { MenuChangeEventBus() }

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
}