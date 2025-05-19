package com.example.mealmatchui.data.api

import retrofit2.http.*

interface MealApiService {
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>
    
    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): Recipe
    
    @POST("recipes/analyze")
    suspend fun analyzeImage(@Body imageData: ImageData): RecipeAnalysis
    
    @POST("recipes")
    suspend fun createRecipe(@Body recipe: Recipe): Recipe
}

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String?
)

data class ImageData(
    val imageBase64: String
)

data class RecipeAnalysis(
    val recipeId: String,
    val confidence: Double,
    val suggestedRecipes: List<Recipe>
) 