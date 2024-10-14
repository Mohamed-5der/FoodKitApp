package com.example.foodkit.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.component.KoinComponent
import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val imageUrl: String = ""
)


class CategoryRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) :KoinComponent {

    fun addCategory(category: Category, imageUri: Uri, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        val imageRef = storage.reference.child("categories/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val updatedCategory = category.copy(imageUrl = uri.toString()) // with image URL
                    // save the category to Firestore With the image URL
                    db.collection("categories").document(updatedCategory.id).set(updatedCategory)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener(onFailure)
                }
            }
            .addOnFailureListener(onFailure)
    }

    fun getCategories(onSuccess: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("categories").get()
            .addOnSuccessListener { documents ->
                val categories = documents.map { it.toObject(Category::class.java) }
                onSuccess(categories)
            }.addOnFailureListener(onFailure)
    }

    fun getFoodsByCategory(categoryId: String, onSuccess: (List<Food>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("categories").document(categoryId).collection("foods").get()
            .addOnSuccessListener { querySnapshot ->
                val foodIds = querySnapshot.documents.mapNotNull { it.getString("foodId") }
                val foodReferences = foodIds.map { id -> db.collection("foods").document(id) }

                // جلب الأطعمة بناءً على معرفات (IDs)
                db.runBatch { batch ->
                    val tasks = foodReferences.map { ref ->
                        ref.get().continueWith { task -> task.result?.toObject(Food::class.java) }
                    }
                    Tasks.whenAllComplete(tasks).addOnSuccessListener { completedTasks ->
                        val foods = completedTasks.mapNotNull { it.result as? Food }
                        onSuccess(foods)
                    }.addOnFailureListener(onFailure)
                }
            }.addOnFailureListener(onFailure)
    }


}
