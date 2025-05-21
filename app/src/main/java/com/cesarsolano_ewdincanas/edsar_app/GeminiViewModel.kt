package com.cesarsolano_ewdincanas.edsar_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val steps: List<String>
)

data class RecipeState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String? = null
)

class GeminiViewModel : ViewModel() {
    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState: StateFlow<RecipeState> = _recipeState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun generateRecipes(mealType: String) {
        // Validación de campo vacío
        if (_searchQuery.value.isBlank()) {
            _recipeState.value = RecipeState(error = "Por favor escriba los ingredientes")
            return
        }

        viewModelScope.launch {
            try {
                _recipeState.value = RecipeState(isLoading = true)

                val prompt = """
                    Actúa como un chef profesional. El usuario tiene los siguientes ingredientes: ${_searchQuery.value}
                    
                    Por favor, proporciona 3 recetas que se puedan hacer con estos ingredientes.
                    Para cada receta, proporciona:
                    1. Nombre de la receta
                    2. Lista de ingredientes necesarios (usando los ingredientes mencionados)
                    3. Pasos detallados para preparar la receta
                    
                    Formatea tu respuesta siguiendo este formato exacto para que pueda ser analizado:
                    
                    RECETA1:Nombre de la primera receta
                    INGREDIENTES1:Ingrediente 1, Ingrediente 2, Ingrediente 3...
                    PASOS1:Paso 1. Detalles|Paso 2. Detalles|Paso 3. Detalles...
                    
                    RECETA2:Nombre de la segunda receta
                    INGREDIENTES2:Ingrediente 1, Ingrediente 2, Ingrediente 3...
                    PASOS2:Paso 1. Detalles|Paso 2. Detalles|Paso 3. Detalles...
                    
                    RECETA3:Nombre de la tercera receta
                    INGREDIENTES3:Ingrediente 1, Ingrediente 2, Ingrediente 3...
                    PASOS3:Paso 1. Detalles|Paso 2. Detalles|Paso 3. Detalles...
                """

                val response = generativeModel.generateContent(prompt)
                val recipes = parseRecipesFromResponse(response)

                _recipeState.value = RecipeState(recipes = recipes)
            } catch (e: Exception) {
                _recipeState.value = RecipeState(error = "Error: ${e.message}")
            }
        }
    }

    private fun parseRecipesFromResponse(response: GenerateContentResponse): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val responseText = response.text?.trim() ?: return emptyList()

        try {
            // Dividir la respuesta en bloques de recetas (cada receta tiene 3 líneas)
            val lines = responseText.split("\n").filter { it.isNotEmpty() }

            var i = 0
            while (i < lines.size) {
                var recipeName = ""
                var ingredients = listOf<String>()
                var steps = listOf<String>()

                // Intentar extraer el nombre de la receta
                if (i < lines.size && lines[i].startsWith("RECETA")) {
                    val parts = lines[i].split(":", limit = 2)
                    if (parts.size > 1) {
                        recipeName = parts[1].trim()
                    }
                    i++
                }

                // Intentar extraer los ingredientes
                if (i < lines.size && lines[i].startsWith("INGREDIENTES")) {
                    val parts = lines[i].split(":", limit = 2)
                    if (parts.size > 1) {
                        ingredients = parts[1].split(",").map { it.trim() }
                    }
                    i++
                }

                // Intentar extraer los pasos
                if (i < lines.size && lines[i].startsWith("PASOS")) {
                    val parts = lines[i].split(":", limit = 2)
                    if (parts.size > 1) {
                        steps = parts[1].split("|").map { it.trim() }
                    }
                    i++
                }

                // Si tenemos al menos nombre y algunos pasos, creamos la receta
                if (recipeName.isNotEmpty() && steps.isNotEmpty()) {
                    recipes.add(Recipe(recipeName, ingredients, steps))
                }

                // Si hay una línea en blanco entre recetas, la saltamos
                if (i < lines.size && lines[i].isBlank()) {
                    i++
                }
            }

            return recipes
        } catch (e: Exception) {
            // Si hay algún error en el análisis, intentamos un enfoque más flexible
            val recipeMatches = Regex("RECETA\\d+:(.+?)\\s+INGREDIENTES\\d+:(.+?)\\s+PASOS\\d+:(.+?)(?=\\s+RECETA|$)", RegexOption.DOT_MATCHES_ALL).findAll(responseText)

            recipeMatches.forEach { match ->
                if (match.groupValues.size >= 4) {
                    val name = match.groupValues[1].trim()
                    val ingredientsList = match.groupValues[2].split(",").map { it.trim() }
                    val stepsList = match.groupValues[3].split("|").map { it.trim() }

                    recipes.add(Recipe(name, ingredientsList, stepsList))
                }
            }

            return recipes
        }
    }
}