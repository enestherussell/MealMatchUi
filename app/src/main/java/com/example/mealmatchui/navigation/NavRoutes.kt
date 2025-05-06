package com.example.mealmatchui.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Main : NavRoutes("main")
    object Search : NavRoutes("search")
    object RecipeDetail : NavRoutes("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object Favorites : NavRoutes("favorites")
} 