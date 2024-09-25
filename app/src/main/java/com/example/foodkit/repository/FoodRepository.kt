package com.example.foodkit.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.component.KoinComponent


// Data Class for Food
data class Food(val id: String = "", val name: String = "", val description: String = "")

class FoodRepository(private val db: FirebaseFirestore) : KoinComponent {

    fun addFood(food: Food, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("foods").add(food)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Food added with ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding food", e)
                onFailure(e)
            }
    }

    fun getFoods(onFoodsLoaded: (List<Food>) -> Unit) {
        db.collection("foods").get()
            .addOnSuccessListener { querySnapshot ->
                val foods = querySnapshot.toObjects(Food::class.java)
                onFoodsLoaded(foods)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting foods", e)
            }
    }
}

