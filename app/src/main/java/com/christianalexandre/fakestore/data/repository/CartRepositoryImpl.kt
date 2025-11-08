package com.christianalexandre.fakestore.data.repository

import android.util.Log
import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepositoryImpl
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
    ) : CartRepository {
        private fun getUserCartCollection() =
            auth.currentUser?.uid?.let { userId ->
                firestore.collection("users").document(userId).collection("cart")
            }

        override fun getCartItems(): Flow<List<CartItem>> =
            callbackFlow {
                val collection = getUserCartCollection()

                if (collection == null) {
                    trySend(emptyList())
                    awaitClose { }
                    return@callbackFlow
                }

                val listener =
                    collection.addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) return@addSnapshotListener
                            close(error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val items = snapshot.toObjects<CartItem>()
                            trySend(items)
                        }
                    }

                awaitClose { listener.remove() }
            }

        override suspend fun addItemToCart(item: CartItem) {
            val collection = getUserCartCollection() ?: return
            val docRef =
                collection.document(item.productId.toString())

            try {
                firestore
                    .runTransaction { transaction ->
                        val snapshot = transaction.get(docRef)

                        if (snapshot.exists()) {
                            val existingItem = snapshot.toObject<CartItem>()!!
                            val newQuantity = existingItem.quantity + item.quantity
                            transaction.update(docRef, "quantity", newQuantity)
                        } else {
                            transaction.set(docRef, item)
                        }
                    }.await()
            } catch (e: Exception) {
                Log.e("CartRepository", "Error adding item:", e)
            }
        }

        override suspend fun removeItemFromCart(productId: Int) {
            val collection = getUserCartCollection() ?: return
            val docRef = collection.document(productId.toString())

            try {
                firestore
                    .runTransaction { transaction ->
                        val snapshot = transaction.get(docRef)

                        if (!snapshot.exists()) {
                            return@runTransaction
                        }

                        val existingItem = snapshot.toObject<CartItem>()!!
                        if (existingItem.quantity > 1) {
                            val newQuantity = existingItem.quantity - 1
                            transaction.update(docRef, "quantity", newQuantity)
                        } else {
                            transaction.delete(docRef)
                        }
                    }.await()
            } catch (e: Exception) {
                Log.e("CartRepository", "Error removing item:", e)
            }
        }

        override suspend fun clearCart() {
            val collection = getUserCartCollection() ?: return

            try {
                val snapshot = collection.get().await()
                if (snapshot.isEmpty) return

                val batch = firestore.batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit().await()
            } catch (e: Exception) {
                Log.e("CartRepository", "Error cleaning cart:", e)
            }
        }
    }
