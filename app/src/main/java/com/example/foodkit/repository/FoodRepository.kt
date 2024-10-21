package com.example.foodkit.repository

import android.net.Uri
import android.util.Log
import com.android.identity.util.UUID
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    var ratingCount: Int = 0,
    var totalSales: Int = 0,
    var category: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val fats: Int = 0,
    var lastWeekRevenue: Double = 0.0,
    val availableQuantity: Int = 1,
    val totalRevenue: Int = 0,
)

class FoodRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) :
    KoinComponent {

    fun addFoodToCategory(
        food: Food,
        imageUri: Uri,
        price: Double,
        categoryId: String,
        categoryName: String,
        availableQuantity: Int,
        calories: Int,
        protein: Int,
        fats: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val imageRef = storage.reference.child("food_images/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val foodWithImage = food.copy(
                    imageUrl = uri.toString(),
                    price = price,
                    category = categoryName,
                    availableQuantity = availableQuantity,
                    calories = calories,
                    protein = protein,
                    fats = fats
                )
                // أضف الطعام إلى Collection العامة foods
                db.collection("foods").add(foodWithImage)
                    .addOnSuccessListener { documentReference ->
                        val foodWithId = foodWithImage.copy(id = documentReference.id)
                        db.collection("foods").document(documentReference.id).set(foodWithId)
                            .addOnSuccessListener {
                                // بعد إضافة الطعام إلى Collection العامة, أضف فقط مرجع (ID الطعام) إلى التصنيف المحدد
                                val foodReference = mapOf("foodId" to foodWithId.id)
                                db.collection("categories").document(categoryId).collection("foods")
                                    .document(foodWithId.id).set(foodReference)
                                    .addOnSuccessListener {
                                        Log.d(
                                            "Firestore",
                                            "Food reference added to category with ID: ${categoryId}"
                                        )
                                        onSuccess()
                                    }.addOnFailureListener { e ->
                                        Log.w(
                                            "Firestore",
                                            "Error adding food reference to category",
                                            e
                                        )
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

    fun updateFoodDetails(
        foodId: String,
        updatedFood: Food,
        name: String,
        description: String,
        imageUri: Uri?,
        price: Double,
        availableQuantity: Int,
        calories: Int,
        protein: Int,
        fats: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val foodRef = db.collection("foods").document(foodId)

        if (imageUri != null) {
            val imageRef = storage.reference.child("food_images/$foodId.jpg")
            val uploadTask = imageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val updatedFoodWithImage = updatedFood.copy(
                        imageUrl = uri.toString(),
                        name = name,
                        description = description,
                        price = price,
                        availableQuantity = availableQuantity,
                        calories = calories,
                        protein = protein,
                        fats = fats
                    )

                    foodRef.set(updatedFoodWithImage)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                }
            }.addOnFailureListener { e -> onFailure(e) }
        } else {
            val updatedFoodWithoutChangeOnImage = updatedFood.copy(
                name = name,
                description = description,
                price = price,
                imageUrl = updatedFood.imageUrl,
                calories = calories,
                protein = protein,
                fats = fats
            )

            foodRef.set(updatedFoodWithoutChangeOnImage)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
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
                val newAverageRating =
                    (currentRating * ratingCount - oldRating + newRating) / ratingCount

                // تحديث متوسط التقييم
                transaction.update(foodDocRef, "rating", newAverageRating)
                transaction.update(userRatingDocRef, "rating", newRating) // تحديث تقييم المستخدم
            } else {
                // إذا كان هذا هو التقييم الأول لهذا المستخدم، قم بزيادة عدد التقييمات
                val newAverageRating = (currentRating * ratingCount + newRating) / (ratingCount + 1)
                transaction.update(foodDocRef, "rating", newAverageRating)
                transaction.update(
                    foodDocRef,
                    "ratingCount",
                    ratingCount + 1
                ) // زيادة عدد التقييمات
                transaction.set(userRatingDocRef, ratingData) // تخزين تقييم المستخدم
            }
        }.addOnSuccessListener {
            Log.d("Firestore", "Rating submitted successfully")
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error submitting rating", exception)
        }
    }

    fun getUserRating(
        foodId: String,
        userId: String,
        onSuccess: (Float?) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
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


    fun getTopFiveFoods(onSuccess: (List<Food>) -> Unit, onFailure: (Exception) -> Unit) {
        // استعلام لجلب الأكلات وترتيبها حسب عدد الطلبات
        db.collection("foods")
            .orderBy("totalSales", Query.Direction.DESCENDING) // ترتيب حسب totalSales
            .limit(5) // تحديد العدد إلى 5
            .get()
            .addOnSuccessListener { querySnapshot ->
                val topFoods = mutableListOf<Food>()

                for (document in querySnapshot.documents) {
                    // تحويل البيانات إلى كائن Food
                    val food = document.toObject(Food::class.java)
                    food?.let { topFoods.add(it) } // إضافة الأكلة إلى القائمة
                }

                onSuccess(topFoods) // إرجاع القائمة عند النجاح
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // إرجاع الخطأ في حالة الفشل
            }
    }

    fun RemoveFromFoods(foodId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        val foodDocRef = FirebaseFirestore.getInstance()
            .collection("foods")
            .document(foodId)

        foodDocRef.update("foods.$foodId", FieldValue.delete())
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}


