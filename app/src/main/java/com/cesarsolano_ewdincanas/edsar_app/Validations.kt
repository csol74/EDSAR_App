package com.cesarsolano_ewdincanas.edsar_app

import android.util.Patterns

//retornar un true si es valido o un false si no es valido
//tambien retorne una cadena que me diga que paso si no es valido

fun validateName(name: String): Pair<Boolean, String> {
    return when {
        name.isEmpty() -> Pair(false, "El nombre siempre es requerido.")
        name.length < 3 -> Pair(false, "El nombre debe tener al menos 3 caracteres.")
        else -> Pair(true, "")
    }
}

fun validateSex(sex: String): Pair<Boolean, String> {
    return when (sex.lowercase()) {
        "hombre", "mujer", "otro" -> Pair(true, "")
        else -> Pair(false, "Sexo debe ser 'hombre', 'mujer' u 'otro'")
    }
}

fun validateWeight(weight: String): Pair<Boolean, String> {
    val number = weight.toIntOrNull()
    return when {
        weight.isEmpty() -> Pair(false, "El peso es obligatorio.")
        number == null || number <= 0 -> Pair(false, "El peso debe ser un número entero positivo.")
        else -> Pair(true, "")
    }
}

fun validateHeight(height: String): Pair<Boolean, String> {
    val value = height.toFloatOrNull()
    return when {
        height.isEmpty() -> Pair(false, "La altura es obligatoria.")
        value == null || value !in 0.5f..2.5f -> Pair(false, "La altura debe estar entre 0.5 y 2.5 metros.")
        else -> Pair(true, "")
    }
}

fun validateAge(age: String): Pair<Boolean, String> {
    val number = age.toIntOrNull()
    return when {
        age.isEmpty() -> Pair(false, "La edad es obligatoria.")
        number == null || number !in 5..120 -> Pair(false, "La edad debe estar entre 5 y 120.")
        else -> Pair(true, "")
    }
}

fun validateEmail(email: String): Pair<Boolean, String> {
    return when {
        email.isEmpty() -> Pair(false, "El correo es obligatorio.")
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(false, "El correo es inválido.")
        !email.endsWith("gmail.com") -> Pair(false, "El correo debe ser @gmail.com.")
        else -> Pair(true, "")
    }
}

fun validatePassword(password: String): Pair<Boolean, String> {
    return when {
        password.isEmpty() -> Pair(false, "La contraseña es obligatoria.")
        password.length < 8 -> Pair(false, "La contraseña debe tener al menos 8 caracteres.")
        !password.any { it.isDigit() } -> Pair(false, "La contraseña debe tener al menos un número.")
        else -> Pair(true, "")
    }
}

fun validateConfirmPassword(password: String, confirmPassword: String): Pair<Boolean, String> {
    return when {
        confirmPassword.isEmpty() -> Pair(false, "La confirmación de contraseña es obligatoria.")
        confirmPassword != password -> Pair(false, "Las contraseñas no coinciden.")
        else -> Pair(true, "")
    }
}
