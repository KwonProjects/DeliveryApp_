package co.kr.kwon.deliveryapp.data.repository.review

import co.kr.kwon.deliveryapp.data.repository.order.Result

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle : String) : Result

    suspend fun writeReview(doc : String,currentTime : String)  : Result

    suspend fun unWrittenReview(uid: String): Result

    //suspend fun reloadReview(doc : String) : Result

}