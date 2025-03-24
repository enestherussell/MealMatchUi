package com.example.mealmatchui.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object Search : NavRoutes("search")
    object RecipeDetail : NavRoutes("recipe/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
} 