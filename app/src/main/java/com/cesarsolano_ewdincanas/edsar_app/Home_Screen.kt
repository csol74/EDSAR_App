package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
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
                            painter = painterResource(id = R.drawable.user_profile),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .size(100.dp)
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
                IconButton(onClick = { onClickProfile() }) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onClickNews() }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onClickRecetas() }) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Menú", modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Ya estás en home */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(30.dp))
                }
            }
        },
        containerColor = Color(0xFFFFA500)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
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

            MealCard(
                title = "¡A Desayunar!",
                description = "¿Estás listo para hacer un desayuno saludable?",
                imageRes = R.drawable.breakfast,
                mealType = "Desayuno",
                onMealSelected = onNavigateToMeal
            )
            MealCard(
                title = "Hora de almorzar",
                description = "Es momento de preparar un almuerzo saludable.",
                imageRes = R.drawable.lunch,
                mealType = "Almuerzo",
                onMealSelected = onNavigateToMeal
            )
            MealCard(
                title = "Vamos a cenar",
                description = "Llegó la noche y con ella tu cena saludable.",
                imageRes = R.drawable.dinner,
                mealType = "Cena",
                onMealSelected = onNavigateToMeal
            )
        }
    }
}

@Composable
fun MealCard(
    title: String,
    description: String,
    imageRes: Int,
    mealType: String,
    onMealSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Black, shape = RoundedCornerShape(40.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = description, color = Color.White, fontSize = 14.sp)
            }
            Button(
                onClick = { onMealSelected(mealType) },
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Ir", color = Color.Black)
            }
        }
    }
}