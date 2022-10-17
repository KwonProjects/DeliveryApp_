package co.kr.kwon.deliveryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationLatLngEntity: LocationLatLngEntity)

    @Query("Select * from LocationLatLngEntity Where id=:id")
    fun get(id: Long): LocationLatLngEntity?

    @Query("Delete from LocationLatLngEntity where id=:id")
    fun delete(id: Long)

}
