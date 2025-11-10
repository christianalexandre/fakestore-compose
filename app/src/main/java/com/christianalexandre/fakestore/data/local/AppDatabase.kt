package com.christianalexandre.fakestore.data.local

//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.christianalexandre.fakestore.data.local.cart.dao.CartItemDao
//import com.christianalexandre.fakestore.data.local.cart.model.CartEntity

/*
    Old cart implementation before migrate the data to FireStore
    I do not delete to think about the future, maybe a local cache
 */
//@Database(entities = [CartEntity::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun cartDao(): CartItemDao
//}