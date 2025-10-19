package com.example.a23100078practicacalificada01.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a23100078practicacalificada01.conversion.ConversionScreen
import com.example.a23100078practicacalificada01.conversion.HistorialScreen
import com.example.a23100078practicacalificada01.presentation.auth.LoginScreen
import com.example.a23100078practicacalificada01.presentation.auth.RegisterScreen

// ---- Rutas centralizadas ----
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CONVERSION = "conversion"
    const val HISTORIAL = "historial"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // ---- Pantalla de Login ----
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.CONVERSION) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ---- Pantalla de Registro ----
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // ---- Pantalla de Conversión ----
        composable(Routes.CONVERSION) {
            ConversionScreen(
                onNavigateToHistorial = {
                    navController.navigate(Routes.HISTORIAL)
                }
            )
        }

        // ---- Pantalla de Historial ----
        composable(Routes.HISTORIAL) {
            HistorialScreen()
        }
    }
}
