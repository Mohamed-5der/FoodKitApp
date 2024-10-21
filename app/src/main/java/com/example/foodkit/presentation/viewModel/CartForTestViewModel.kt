package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.repository.CartItem
import com.example.foodkit.repository.CartRepository
import com.example.foodkit.repository.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartForTestViewModel(private val repository: CartRepository) : ViewModel() {

    // قائمة منتجات العربة


    private var _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    // إجمالي السعر
    val totalPriceState: StateFlow<Double> = _cartItems.map { items ->
        repository.calculateTotalPrice(items)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0.0
    )

    // تحميل المنتجات من العربة
    fun loadCartItems(userId: String) {
        repository.getCartItems(userId,
            onSuccess = { items ->
                Log.d("CartViewModel", "Loaded cart items: $items")
                _cartItems.value = items
            },
            onFailure = { exception ->
                Log.e("CartViewModel", "Failed to load cart items: ${exception.message}")
            }
        )
    }

    // إضافة منتج إلى العربة
    fun addToCart(food: Food, quantity: Int = 1, userId: String, onAddToCartSuccess: () -> Unit) {
        val existingItem = _cartItems.value.find { it.foodId == food.id }

        if (existingItem != null) {
            // إذا كان العنصر موجودًا، قم بتحديث الكمية
            val newQuantity = existingItem.quantity + quantity // حساب الكمية الجديدة
            updateItemQuantityWhenAddToCart(existingItem.foodId, userId, newQuantity)
        } else {
            // إذا لم يكن موجودًا، قم بإضافته إلى السلة
            val newItem = CartItem(
                foodId = food.id,
                foodName = food.name,
                foodPrice = food.price,
                quantity = quantity
            )

            // إضافة العنصر الجديد للسلة في قاعد�� البيانات
            repository.addToCart(food, userId, quantity,
                onSuccess = {
                    Log.d("CartViewModel", "Adding to cart: ${food.name} with quantity: $quantity")
                    // تحديث الحالة مباشرةً
                    _cartItems.value += newItem
                    onAddToCartSuccess()
                },
                onFailure = { exception ->
                    Log.e("CartViewModel", "Failed to add item to cart: ${exception.message}")
                }
            )
        }
    }

    // تحديث كمية المنتج
    private fun updateItemQuantityWhenAddToCart(foodId: String, userId: String, newQuantity: Int) {
        val currentItem = _cartItems.value.find { it.foodId == foodId }
        if (currentItem != null) {
            val updatedQuantity = newQuantity  // استخدم الكمية الجديدة مباشرة
            repository.updateCartItemQuantityWhenAddToCart(
                foodId = foodId,
                userId = userId,
                newQuantity = updatedQuantity,  // تمرير الكمية الجديدة المحددة فقط
                foodName = currentItem.foodName,
                foodPrice = currentItem.foodPrice,
                foodImageUrl = currentItem.imageUrl,
                onSuccess = {
                    // تحديث العناصر في الذاكرة بعد نجاح التحديث
                    _cartItems.value = _cartItems.value.map {
                        if (it.foodId == foodId) it.copy(quantity = updatedQuantity) else it
                    }
                    Log.d("CartViewModel", "Item quantity updated successfully")
                },
                onFailure = { exception ->
                    Log.e("CartViewModel", "Failed to update item quantity: ${exception.message}")
                }
            )
        }
    }


    // حذف منتج من العربة
    fun removeFromCart(foodId: String, userId: String) {
        repository.removeFromCart(foodId, userId,
            onSuccess = {
                // إعادة تحميل المنتجات بعد الحذف
                loadCartItems(userId)
            },
            onFailure = { exception ->
                Log.e("CartViewModel", "Failed to remove item from cart: ${exception.message}")
            }
        )
    }

    fun clearCart(userId: String) {
        repository.clearCart(userId,
            onSuccess = {
                loadCartItems(userId)
            },
            onFailure = { exception ->
                Log.e("CartViewModel", "Failed to clear cart: ${exception.message}")
            }
        )
    }

    fun updateQuantityInCartScreen(foodId: String, userId: String, increment: Int) {
        repository.updateQuantityInCartScreen(foodId, userId, increment,
            onSuccess = {
                // تحديث العناصر بعد تعديل الكمية
                loadCartItems(userId)
            },
            onFailure = { exception ->
                Log.e("CartViewModel", "Failed to update quantity: ${exception.message}")
            }
        )
    }


    // إرسال الطلب
    fun checkout(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        if (_cartItems.value.isNotEmpty()) {
            repository.submitOrder(
                _cartItems.value, userId,
                onSuccess = {
                    _cartItems.value = emptyList() // تفريغ العربة بعد الطلب
                    onSuccess()
                },
                onFailure = { exception ->
                    onFailure(exception)
                    Log.e("CartViewModel", "Failed to submit order: ${exception.message}")
                }
            )
        }
    }
}