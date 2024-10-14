package com.example.foodkit.repository

import android.net.Uri
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
            }
            .addOnFailureListener(onFailure)
    }
}
