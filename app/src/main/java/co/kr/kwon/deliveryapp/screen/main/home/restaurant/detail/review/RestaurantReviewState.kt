package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.review


import co.kr.kwon.deliveryapp.model.review.RestaurantReviewModel

sealed class RestaurantReviewState {

    object Uninitialized : RestaurantReviewState()

    object Loading : RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>
    ) : RestaurantReviewState()
}

