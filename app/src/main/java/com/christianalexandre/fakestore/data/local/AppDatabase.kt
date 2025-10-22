package com.christianalexandre.fakestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.christianalexandre.fakestore.data.local.cart.dao.CartItemDao
import com.christianalexandre.fakestore.data.local.cart.model.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartItemDao
}