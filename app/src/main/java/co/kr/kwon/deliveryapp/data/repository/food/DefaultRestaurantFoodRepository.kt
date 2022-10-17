package co.kr.kwon.deliveryapp.data.repository.food

import co.kr.kwon.deliveryapp.data.db.dao.FoodMenuBasketDao
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService : FoodApiService,
    private val foodMenuBasketDao: FoodMenuBasketDao,
    private val dispatcher: CoroutineDispatcher
) : RestaurantFoodRepository {

    override suspend fun getFoods(restaurantId: Long, restaurantTitle: String,restaurantImageUrl : String): List<RestaurantFoodEntity>  = withContext(dispatcher){
        val response = foodApiService.getRestaurantFoods(restaurantId)
        if (response.isSuccessful){
            response.body()?.map {
                it.toEntity(restaurantId,restaurantTitle,restaurantImageUrl)
            } ?: listOf()
        } else{
            listOf()
        }
    }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity> = withContext(dispatcher){
        foodMenuBasketDao.getAll()
    }

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> = withContext(dispatcher) {
        foodMenuBasketDao.getAllByRestaurantId(restaurantId)
    }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity)  = withContext(dispatcher){
        foodMenuBasketDao.insert(restaurantFoodEntity)
    }

    override suspend fun insertFoodsMenuInBasket(restaurantFoodsEntity: List<RestaurantFoodEntity>)  = withContext(dispatcher){
        foodMenuBasketDao.insertList(restaurantFoodsEntity)
    }

    override suspend fun removeFoodMenuListInBasket(foodId: String) = withContext(dispatcher){
        foodMenuBasketDao.delete(foodId)
    }

    override suspend fun clearFoodMenuListInBasket() = withContext(dispatcher){
        foodMenuBasketDao.deleteAll()
    }



}