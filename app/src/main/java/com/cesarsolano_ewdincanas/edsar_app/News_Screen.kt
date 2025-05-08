package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun News_Screen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Notificaciones y novedades", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFFFA500)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Endrick Felipe Moreira",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            NotificationCard(
                title = "¡Nueva receta saludable disponible!",
                message = "💪 Prepara un bowl proteico en solo 10 minutos. ¡Ideal para después del gym!"
            )

            NotificationCard(
                title = "Inspírate hoy con algo fresco",
                message = "🥬 Descubre nuestra nueva ensalada detox con aderezo casero. Ligera, nutritiva y deliciosa."
            )

            NotificationCard(
                title = "¡Receta fit recién salida del horno!",
                message = "🍌 ¿Ya probaste el banana bread sin azúcar? Corre a verla antes de que se enfríe."
            )

            NotificationCard(
                title = "Receta fit del día 🌻",
                message = "🥄 Prepara un smoothie tropical rico en fibra y sin azúcares añadidos. ¡Tu cuerpo te lo va a agradecer!"
            )
        }
    }
}

@Composable
fun NotificationCard(title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(color = Color(0xFFF1F1F1), shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = message, fontSize = 14.sp, color = Color.Gray)
    }
}
