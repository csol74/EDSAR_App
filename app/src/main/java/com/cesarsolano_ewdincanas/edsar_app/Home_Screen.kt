package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickLogout: () -> Unit = {},
    onClickProfile: () -> Unit = {},
    onClickNews: () -> Unit = {},
    onNavigateToMeal: (String) -> Unit = {},
    onClickRecetas: () -> Unit = {}
) {
    var showLogout by remember { mutableStateOf(false) }

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    var userName by remember { mutableStateOf("Usuario") }

    // Firebase Firestore user data
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            Firebase.firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(UserModel::class.java)
                    userName = user?.name ?: "Usuario"
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.edsar_logo),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { showLogout = !showLogout },
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hola, ", color = Color.White)
                        Text(userName, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { /* acción menú */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onClickProfile() }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.size(30.dp))
                    }
                    IconButton(onClick = { onClickNews() }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(30.dp))
                    }
                    IconButton(onClick = { onClickRecetas() }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Menú", modifier = Modifier.size(30.dp))
                    }
                }
            }
        },
        containerColor = Color(0xFFFFA500)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (showLogout) {
                Button(
                    onClick = { onClickLogout() },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Cerrar Sesión", color = Color.Black)
                }
            }

            // Distribución proporcional para cada tarjeta
            Column(modifier = Modifier.fillMaxHeight()) {
                MealCard(
                    title = "¡A Desayunar!",
                    description = "¿Estás listo para hacer un desayuno saludable?",
                    imageRes = R.drawable.tosti,
                    mealType = "Desayuno",
                    onMealSelected = onNavigateToMeal,
                    modifier = Modifier.weight(1f)
                )
                MealCard(
                    title = "Hora de almorzar",
                    description = "Es momento de preparar un almuerzo saludable.",
                    imageRes = R.drawable.lol,
                    mealType = "Almuerzo",
                    onMealSelected = onNavigateToMeal,
                    modifier = Modifier.weight(1f)
                )
                MealCard(
                    title = "Vamos a cenar",
                    description = "Llegó la noche y con ella tu cena saludable.",
                    imageRes = R.drawable.dinner,
                    mealType = "Cena",
                    onMealSelected = onNavigateToMeal,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MealCard(
    title: String,
    description: String,
    imageRes: Int,
    mealType: String,
    onMealSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 10.dp) // Menor separación entre tarjetas
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp), // Padding interno reducido
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen más pequeña y contenida
            Box(
                modifier = Modifier
                    .size(100.dp) // <-- antes era 75dp
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 0.dp)
            ) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp // Compacto
                )
                Text(
                    text = description,
                    color = Color.Black,
                    fontSize = 14.sp // Compacto
                )
            }

            Button(
                onClick = { onMealSelected(mealType) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(44.dp) // Más bajo
                    .width(70.dp), // Más angosto
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Ir", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
