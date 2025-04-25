package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun RegisterScreen() {
    val campos = listOf(
        "Nombres",
        "Apellidos",
        "Sexo, solo masculino o femenino",
        "Digita tu peso en Kg",
        "Digita tu altura en metros",
        "Digita tu edad",
        "Correo electrónico",
        "Contraseña",
        "Confirma la contraseña"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA500)) // Fondo naranja
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.edsar_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Text("EDSAR", fontSize = 24.sp, color = Color.White)
        Text("FITNESS", fontSize = 16.sp, color = Color.Green)

        Spacer(modifier = Modifier.height(24.dp))

        // Caja negra del formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, shape = RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            campos.forEach { campo ->
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(campo, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(50.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp),
                    singleLine = true
                )

            }
        }
    }
}
