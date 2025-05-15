package com.cesarsolano_ewdincanas.edsar_app

import android.content.Context
import com.cesarsolano_ewdincanas.edsar_app.models.Receta
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object RecetaUploader {

    fun cargarRecetasDesdeJson(context: Context): List<Receta> {
        val inputStream = context.assets.open("recetas.json")
        val reader = InputStreamReader(inputStream)
        val recetaType = object : TypeToken<List<Receta>>() {}.type
        return Gson().fromJson(reader, recetaType)
    }

    fun subirRecetasDesdeJson(context: Context) {
        val firestore = FirebaseFirestore.getInstance()
        val recetas = cargarRecetasDesdeJson(context)

        recetas.forEach { receta ->
            firestore.collection("recetas").document(receta.titulo)
                .set(receta)
                .addOnSuccessListener {
                    println("✅ Receta subida: ${receta.titulo}")
                }
                .addOnFailureListener {
                    println("❌ Error subiendo receta: ${receta.titulo}")
                }
        }
    }
}