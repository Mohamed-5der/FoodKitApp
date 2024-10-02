package com.example.foodkit.repository

import android.net.Uri
import android.util.Log
import com.android.identity.util.UUID
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.component.KoinComponent


// Data Class for Food
data class Food(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    var rating: Float = 0f,
    var ratingCount: Int = 0
)

class FoodRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) : KoinComponent {

    fun addFoodToCategory(food: Food, imageUri: Uri, price: Double, categoryId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val imageRef = storage.reference.child("food_images/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val foodWithImage = food.copy(imageUrl = uri.toString(), price = price)
                // أضف الطعام إلى Collection العامة foods
                db.collection("foods").add(foodWithImage)
                    .addOnSuccessListener { documentReference ->
                        val foodWithId = foodWithImage.copy(id = documentReference.id)
                        db.collection("foods").document(documentReference.id).set(foodWithId)
                            .addOnSuccessListener {
                                // بعد إضافة الطعام إلى Collection العامة, أضفه إلى التصنيف المحدد
                                db.collection("categories").document(categoryId).collection("foods").document(foodWithId.id).set(foodWithId)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Food added to category with ID: ${categoryId}")
                                        onSuccess()
                                    }.addOnFailureListener { e ->
                                        Log.w("Firestore", "Error adding food to category", e)
                                        onFailure(e)
                                    }
                            }.addOnFailureListener { e ->
                                Log.w("Firestore", "Error updating food ID", e)
                                onFailure(e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding food", e)
                        onFailure(e)
                    }
            }
        }.addOnFailureListener { e ->
            Log.w("Storage", "Error uploading image", e)
            onFailure(e)
        }
    }


    fun getFoodById(foodId: String, onFoodLoaded: (Food) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("foods").document(foodId).get()
            .addOnSuccessListener { documentSnapshot ->
                val food = documentSnapshot.toObject(Food::class.java)
                food?.let(onFoodLoaded)
            }.addOnFailureListener(onFailure)
    }

    fun submitRating(foodId: String, userId: String, newRating: Float) {
        val ratingData = mapOf("foodId" to foodId, "userId" to userId, "rating" to newRating)
        val userRatingDocRef = db.collection("food_ratings").document("$foodId-$userId")
        val foodDocRef = db.collection("foods").document(foodId)

        db.runTransaction { transaction ->
            val foodSnapshot = transaction.get(foodDocRef)
            val currentRating = foodSnapshot.getDouble("rating")?.toFloat() ?: 0f
            val ratingCount = foodSnapshot.getLong("ratingCount")?.toInt() ?: 0

            // تحقق مما إذا كان المستخدم قد قدم تقييمًا سابقًا
            val userRatingSnapshot = transaction.get(userRatingDocRef)

            if (userRatingSnapshot.exists()) {
                // إذا كان المستخدم قد قدم تقييمًا مسبقًا، احسب المتوسط الجديد
                val oldRating = userRatingSnapshot.getDouble("rating")?.toFloat() ?: 0f
                val newAverageRating = (currentRating * ratingCount - oldRating + newRating) / ratingCount

                // تحديث متوسط التقييم
                transaction.update(foodDocRef, "rating", newAverageRating)
                transaction.update(userRatingDocRef, "rating", newRating) // تحديث تقييم المستخدم
            } else {
                // إذا كان هذا هو التقييم الأول لهذا المستخدم، قم بزيادة عدد التقييمات
                val newAverageRating = (currentRating * ratingCount + newRating) / (ratingCount + 1)
                transaction.update(foodDocRef, "rating", newAverageRating)
                transaction.update(foodDocRef, "ratingCount", ratingCount + 1) // زيادة عدد التقييمات
                transaction.set(userRatingDocRef, ratingData) // تخزين تقييم المستخدم
            }
        }.addOnSuccessListener {
            Log.d("Firestore", "Rating submitted successfully")
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error submitting rating", exception)
        }
    }




    fun getUserRating(foodId: String, userId: String, onSuccess: (Float?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("food_ratings").document("$foodId-$userId").get()
            .addOnSuccessListener { documentSnapshot ->
                val rating = documentSnapshot.getDouble("rating")?.toFloat()
                onSuccess(rating)
            }
            .addOnFailureListener(onFailure)
    }

    fun getAllFoods(onSuccess: (List<Food>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("foods").get()
            .addOnSuccessListener { querySnapshot ->
                val foods = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Food::class.java)?.apply {

                        ratingCount = document.getLong("ratingCount")?.toInt() ?: 0
                        rating = document.getDouble("rating")?.toFloat() ?: 0f
                    }
                }
                onSuccess(foods)
            }
            .addOnFailureListener(onFailure)
    }
}


