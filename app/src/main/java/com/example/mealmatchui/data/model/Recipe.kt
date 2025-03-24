package com.example.mealmatchui.data.model

enum class RecipeCategory {
    ALL,
    MEAT,
    VEGETABLE,
    VEGAN,
    DESSERT;

    fun toDisplayName(): String {
        return when (this) {
            ALL -> "Tümü"
            MEAT -> "Et"
            VEGETABLE -> "Sebze"
            VEGAN -> "Vegan"
            DESSERT -> "Tatlı"
        }
    }
}

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean = false
) 