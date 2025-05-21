package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cesarsolano_ewdincanas.edsar_app.models.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    navController: NavController,
    recetaNombre: String,
    viewModel: GeminiViewModel
) {
    val context = LocalContext.current
    var receta by remember { mutableStateOf<Receta?>(null) }
    val recipeState by viewModel.recipeState.collectAsState()

    LaunchedEffect(Unit) {
        val todasLasRecetas = RecetaUploader.cargarRecetasDesdeJson(context)
        receta = todasLasRecetas.find { it.titulo == recetaNombre }
        receta?.let {
            viewModel.updateSearchQuery(it.titulo)
            viewModel.generateProcedimientoDesdeTitulo(it.titulo)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la receta", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFFFA500)
    ) { padding ->
        receta?.let { r ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = cardElevation(8.dp),
                        colors = cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = r.imagenUrl,
                                contentDescription = r.titulo,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = r.titulo,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Mostrar resultado según estado de carga de Gemini
                if (recipeState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                } else if (recipeState.error != null) {
                    item {
                        Text(
                            text = "Error: ${recipeState.error}",
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else if (recipeState.recipes.isNotEmpty()) {
                    val recetaGenerada = recipeState.recipes.find {
                        it.name.equals(r.titulo, ignoreCase = true)
                    }

                    recetaGenerada?.let { recetaDetallada ->
                        item {
                            Text(
                                text = "Ingredientes",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        items(recetaDetallada.ingredients) { ingrediente ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = cardColors(containerColor = Color.White),
                                elevation = cardElevation(2.dp)
                            ) {
                                Text(
                                    text = "• $ingrediente",
                                    modifier = Modifier.padding(12.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Pasos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        itemsIndexed(recetaDetallada.steps) { index, paso ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = cardColors(containerColor = Color.White),
                                elevation = cardElevation(2.dp)
                            ) {
                                Text(
                                    text = "${index + 1}. $paso",
                                    modifier = Modifier.padding(12.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}

