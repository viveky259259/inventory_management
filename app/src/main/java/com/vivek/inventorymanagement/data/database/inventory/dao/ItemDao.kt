package com.vivek.inventorymanagement.data.database.inventory.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vivek.inventorymanagement.data.database.inventory.entities.ItemEntity

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<ItemEntity>

    @Insert
    fun insertAll(items: List<ItemEntity>)

    @Delete
    fun delete(item: ItemEntity)
}