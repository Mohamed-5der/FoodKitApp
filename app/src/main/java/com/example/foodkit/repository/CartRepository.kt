package com.example.foodkit.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.component.KoinComponent

data class CartItem(
    val foodId: String = "",
    val foodName: String = "",
    val foodPrice: Double = 0.0,
    var quantity: Int = 1,
    val userId: String = ""
)

data class Order(
    val id: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)

class CartRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) :KoinComponent {

    fun addToCart(
        food: Food,
        userId: String,
        newQuantity: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val cartDocRef = FirebaseFirestore.getInstance()
            .collection("carts")
            .document(userId)

        cartDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // تحقق مما إذا كانت الوثيقة موجودة والعناصر موجودة
                val items = document.get("items") as? Map<*, *>
                val currentItem = items?.get(food.id) as? Map<*, *>

                if (currentItem != null) {
                    // إذا كان العنصر موجودًا، نحصل على الكمية الحالية ونجمعها مع الكمية الجديدة
                    val currentQuantity = currentItem["quantity"] as? Long ?: 0
                    val updatedQuantity = currentQuantity + newQuantity

                    // تحديث الكمية الجديدة
                    cartDocRef.update("items.${food.id}.quantity", updatedQuantity)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }
                } else {
                    // إذا لم يكن العنصر موجودًا، نضيفه مع الكمية الجديدة
                    val cartItem = hashMapOf(
                        "foodId" to food.id,
                        "foodName" to food.name,
                        "foodPrice" to food.price,
                        "quantity" to newQuantity
                    )
                    cartDocRef.update("items.${food.id}", cartItem)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }
                }
            } else {
                // إذا لم تكن الوثيقة موجودة، نقوم بإنشاء وثيقة جديدة وإضافة العنصر
                val cartItem = hashMapOf(
                    "items" to hashMapOf(
                        food.id to hashMapOf(
                            "foodId" to food.id,
                            "foodName" to food.name,
                            "foodPrice" to food.price,
                            "quantity" to newQuantity
                        )
                    )
                )
                cartDocRef.set(cartItem)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }


    fun getCartItems(userId: String, onSuccess: (List<CartItem>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("carts").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // get the items field as a Map<String, Map<String, Any>>
                    val items = document.get("items") as? Map<String, Map<String, Any>> ?: emptyMap()

                    // تحويل العناصر إلى قائمة من CartItem
                    val cartItems = items.map { (foodId, item) ->
                        CartItem(
                            foodId = foodId,
                            foodName = item["foodName"] as? String ?: "",
                            foodPrice = (item["foodPrice"] as? Number)?.toDouble() ?: 0.0,
                            quantity = (item["quantity"] as? Number)?.toInt() ?: 1,
                            userId = userId
                        )
                    }
                    onSuccess(cartItems)
                } else {
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CartRepository", "Failed to load cart items: ${exception.message}")
                onFailure(exception)
            }
    }


    fun updateCartItemQuantityWhenAddToCart(
        foodId: String,
        userId: String,
        newQuantity: Int,
        foodName: String,
        foodPrice: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val cartDocRef = FirebaseFirestore.getInstance()
            .collection("carts")
            .document(userId)

        cartDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val items = document.data?.get("items") as? Map<*, *>
                val currentItem = items?.get(foodId) as? Map<*, *>

                if (currentItem != null) {
                    // تحديث الكمية فقط بدون جمع الكمية القديمة
                    cartDocRef.update("items.$foodId.quantity", newQuantity)
                        .addOnSuccessListener {
                            Log.d("CartRepository", "Item quantity updated successfully")
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Log.e("CartRepository", "Failed to update item quantity: ${it.message}")
                            onFailure(it)
                        }
                } else {
                    // إذا لم يكن العنصر موجودًا، نضيفه مع الكمية الجديدة
                    val newItem = hashMapOf(
                        "quantity" to newQuantity,
                        "foodName" to foodName,
                        "foodPrice" to foodPrice
                    )
                    cartDocRef.update("items.$foodId", newItem)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }
                }
            } else {
                // إذا لم تكن الوثيقة موجودة، قم بإنشاء سلة جديدة
                val newCart = hashMapOf(
                    "items" to hashMapOf(foodId to hashMapOf(
                        "quantity" to newQuantity,
                        "foodName" to foodName,
                        "foodPrice" to foodPrice
                    ))
                )
                cartDocRef.set(newCart)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }



    fun calculateTotalPrice(cartItems: List<CartItem>): Double {
        return cartItems.sumOf { it.foodPrice * it.quantity }
    }

    fun removeFromCart(foodId: String, userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cartDocRef = FirebaseFirestore.getInstance()
            .collection("carts")
            .document(userId)

        cartDocRef.update("items.$foodId", FieldValue.delete())
            .addOnSuccessListener {
                Log.d("CartRepository", "Item removed successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("CartRepository", "Failed to remove item from cart: ${exception.message}")
                onFailure(exception)
            }
    }

    fun clearCart(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // الحصول على مرجع الوثيقة الخاصة بالسلة
        val cartDocRef = db.collection("carts").document(userId)

        cartDocRef.delete() // حذف الوثيقة بأكملها
            .addOnSuccessListener {
                Log.d("CartRepository", "Cart cleared successfully")
                onSuccess()
            }
            .addOnFailureListener(onFailure)
    }

    fun updateQuantityInCartScreen(foodId: String, userId: String, increment: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cartDocRef = FirebaseFirestore.getInstance()
            .collection("carts")
            .document(userId)

        cartDocRef.update("items.$foodId.quantity", FieldValue.increment(increment.toLong()))
            .addOnSuccessListener {
                Log.d("CartRepository", "Quantity updated successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("CartRepository", "Failed to update quantity: ${exception.message}")
                onFailure(exception)
            }
    }


    fun submitOrder(cartItems: List<CartItem>, userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        val totalPrice = calculateTotalPrice(cartItems)

        val order = hashMapOf(
            "userId" to userId,
            "items" to cartItems,
            "totalPrice" to totalPrice,
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("orders").add(order)
            .addOnSuccessListener {
                Log.d("CartRepository", "Order submitted successfully")
                onSuccess()
            }
            .addOnFailureListener(onFailure)
    }


    fun getOrders(onSuccess: (List<Order>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("orders").get()
            .addOnSuccessListener { querySnapshot ->
                val orders = querySnapshot.documents.mapNotNull { document ->
                    val order = document.toObject(Order::class.java)
                    order?.copy(id = document.id)  //  take ID From Document Reference
                }
                onSuccess(orders)
            }
            .addOnFailureListener(onFailure)
    }


}
