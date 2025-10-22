package com.christianalexandre.fakestore.data.local.cart.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.christianalexandre.fakestore.data.local.cart.model.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateItem(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteItem(productId: Int)

    @Query("SELECT * FROM cart_items ORDER BY productId ASC")
    fun getCartItems(): Flow<List<CartEntity>>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getItemById(productId: Int): CartEntity?
}