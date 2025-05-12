package com.example.mealmatchui.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Main : NavRoutes("main")
    object Search : NavRoutes("search")
    object RecipeDetail : NavRoutes("recipe/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
    object Favorites : NavRoutes("favorites")
    object Camera : NavRoutes("camera")
    object RecipeResult : NavRoutes("recipe_result/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_result/$recipeId"
    }
} 