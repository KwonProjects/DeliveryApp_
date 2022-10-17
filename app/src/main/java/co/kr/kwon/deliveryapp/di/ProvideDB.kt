package co.kr.kwon.deliveryapp.di

import android.content.Context
import androidx.room.Room
import co.kr.kwon.deliveryapp.data.db.AppDatabase

fun provideDB(context: Context) : AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()

fun provideLocationDao(database: AppDatabase) = database.LocationDao()

fun provideRestaurantDao(database: AppDatabase) = database.RestaurantDao()

fun provideFoodMenuBasketDao(database: AppDatabase) = database.FoodMenuBasketDao()