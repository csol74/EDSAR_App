package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Extensión para obtener el ViewModel en la composición
 */
@Composable
fun geminiViewModel(): GeminiViewModel {
    return viewModel()
}