package com.example.mealmatchui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mealmatchui.navigation.NavRoutes
import com.example.mealmatchui.ui.screens.*
import com.example.mealmatchui.ui.theme.MealMatchTheme
import com.example.mealmatchui.ui.viewmodel.RecipeViewModel
import com.example.mealmatchui.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealMatchTheme {
                MealMatchApp()
            }
        }
    }
}

@Composable
fun MealMatchApp() {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
        composable(NavRoutes.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(NavRoutes.Register.route) },
                onLoginSuccess = { navController.navigate(NavRoutes.Main.route) },
                viewModel = userViewModel
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(NavRoutes.Login.route) },
                onRegisterSuccess = { navController.navigate(NavRoutes.Main.route) },
                viewModel = userViewModel
            )
        }

        composable(NavRoutes.Main.route) {
            MainScreen(
                onSearchClick = { navController.navigate(NavRoutes.Search.route) },
                onRecipeClick = { recipeId -> 
                    navController.navigate(NavRoutes.RecipeDetail.createRoute(recipeId))
                },
                viewModel = recipeViewModel
            )
        }

        composable(NavRoutes.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onRecipeClick = { recipeId -> 
                    navController.navigate(NavRoutes.RecipeDetail.createRoute(recipeId))
                },
                viewModel = recipeViewModel
            )
        }

        composable(
            route = NavRoutes.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: return@composable
            RecipeDetailScreen(
                recipeId = recipeId,
                onNavigateBack = { navController.popBackStack() },
                onFavoriteClick = { recipeViewModel.toggleFavorite(recipeId) }
            )
        }

        composable(NavRoutes.Favorites.route) {
            FavoritesScreen(
                onRecipeClick = { recipeId -> 
                    navController.navigate(NavRoutes.RecipeDetail.createRoute(recipeId))
                },
                viewModel = recipeViewModel
            )
        }
    }
}