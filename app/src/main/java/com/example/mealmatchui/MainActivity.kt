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
import com.example.mealmatchui.ui.screens.MainScreen
import com.example.mealmatchui.ui.screens.RecipeDetailScreen
import com.example.mealmatchui.ui.screens.SearchScreen
import com.example.mealmatchui.ui.theme.MealMatchTheme
import com.example.mealmatchui.ui.viewmodel.RecipeViewModel

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
    val viewModel: RecipeViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRoutes.Main.route) {
        composable(NavRoutes.Main.route) {
            MainScreen(
                onSearchClick = { navController.navigate(NavRoutes.Search.route) },
                onRecipeClick = { recipeId -> 
                    navController.navigate(NavRoutes.RecipeDetail.createRoute(recipeId))
                },
                viewModel = viewModel
            )
        }

        composable(NavRoutes.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onRecipeClick = { recipeId -> 
                    navController.navigate(NavRoutes.RecipeDetail.createRoute(recipeId))
                },
                viewModel = viewModel
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
                onFavoriteClick = { viewModel.toggleFavorite(recipeId) }
            )
        }
    }
}