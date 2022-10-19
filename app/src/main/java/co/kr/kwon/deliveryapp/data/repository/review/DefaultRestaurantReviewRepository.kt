package co.kr.kwon.deliveryapp.data.repository.review

import android.net.Uri
import androidx.lifecycle.lifecycleScope
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.data.entity.RestaurantReviewEntity
import co.kr.kwon.deliveryapp.data.entity.ReviewEntity
import co.kr.kwon.deliveryapp.data.repository.order.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DefaultRestaurantReviewRepository(
    private val dispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : RestaurantReviewRepository {

    override suspend fun getReviews(restaurantTitle: String): Result =
        withContext(dispatcher) {
            return@withContext try {
                val snapshot = firestore
                    .collection("review")
                    .whereEqualTo("restaurantTitle", restaurantTitle)
                    .get()
                    .await()
                Result.Success(snapshot.documents.map {
                    ReviewEntity(
                        userId = it.get("userId") as String,
                        title = it.get("title") as String,
                        createdAt = it.get("createdAt") as Long,
                        content = it.get("content") as String,
                        rating = (it.get("rating") as Double).toFloat(),
                        imageUrlList = it.get("imageUrlList") as? List<String>,
                        orderId = it.get("orderId") as String,
                        restaurantTitle = it.get("restaurantTitle") as String
                    )
                })
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e)
            }
        }

    override suspend fun writeReview(doc: String, currentTime: String): Result =
        withContext(dispatcher) {

            val result: Result = try {
                firestore
                    .collection("order")
                    .document(doc)
                    .update("writeReview", false)
                Result.Success<Any>()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e)
            }
            return@withContext result
        }

    //unWrittenReview
    override suspend fun unWrittenReview(userId: String): Result = withContext(dispatcher) {
        return@withContext try {
            val result: QuerySnapshot = firestore
                .collection("order")
                .whereEqualTo("userId", userId)
                .whereEqualTo("writeReview",true)
                .get()
                .await()
            Result.Success(result.documents.map {
                OrderEntity(
                    date = it.get("date") as Long,
                    id = it.id,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map { food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            description = food["description"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurantId"] as Long,
                            restaurantTitle = food["restaurantTitle"] as String,
                            restaurantImageUrl = food["restaurantImageUrl"] as String
                        )
                    },
                    writeReview = it.get("writeReview") as Boolean
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

}