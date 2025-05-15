package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Meals_Screen(
    mealType: String,
    onBackToHome: () -> Unit,
    viewModel: GeminiViewModel = geminiViewModel()
) {
    val recipeState by viewModel.recipeState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¡$mealType!") },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFFFA500)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("¿Qué ingredientes tienes para hoy?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categorías
            val categories = listOf("Proteínas Animales", "Proteínas Vegetales", "Vegetales", "Carbohidratos")
            categories.forEach {
                CategoryItem(it)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.generateRecipes() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Quiero mi receta!", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Estado de carga
            if (recipeState.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            // Error message
            recipeState.error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Mostrar las recetas
            if (recipeState.recipes.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(recipeState.recipes) { recipe ->
                        RecipeCard(recipe)
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "🍽️ ${recipe.name}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingredientes:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            recipe.ingredients.forEach { ingredient ->
                Text("• $ingredient")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preparación:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            recipe.steps.forEach { step ->
                Text(text = step, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}

@Composable
fun CategoryItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Black, RoundedCornerShape(15.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Button(
            onClick = { /* Puedes mostrar una lista de ingredientes */ },
            shape = CircleShape,
            modifier = Modifier.size(30.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("v", color = Color.Black)
        }
    }
}