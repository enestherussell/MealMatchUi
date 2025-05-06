package com.example.mealmatchui.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mealmatchui.data.model.Recipe
import com.example.mealmatchui.data.model.RecipeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val _selectedCategory = MutableStateFlow<RecipeCategory?>(null)
    val selectedCategory: StateFlow<RecipeCategory?> = _selectedCategory.asStateFlow()

    private val _recipes = mutableStateOf(listOf(
        Recipe(
            id = "1",
            name = "Mantı",
            category = RecipeCategory.MEAT.name,
            ingredients = listOf("Un", "Yumurta", "Kıyma", "Yoğurt", "Sarımsak"),
            instructions = listOf(
                "Hamur yoğrulur",
                "Açılır",
                "Kesilir",
                "Doldurulur",
                "Pişirilir"
            ),
            imageUrl = "https://example.com/manti.jpg",
            isFavorite = false
        ),
        Recipe(
            id = "2",
            name = "Karnıyarık",
            category = RecipeCategory.MEAT.name,
            ingredients = listOf("Patlıcan", "Kıyma", "Soğan", "Sarımsak", "Domates"),
            instructions = listOf(
                "Patlıcanlar kızartılır",
                "Kıyma hazırlanır",
                "Patlıcanlar doldurulur",
                "Fırında pişirilir"
            ),
            imageUrl = "https://example.com/karniyarik.jpg",
            isFavorite = true
        ),
        Recipe(
            id = "3",
            name = "İskender",
            category = RecipeCategory.MEAT.name,
            ingredients = listOf("Döner", "Pide", "Domates Sosu", "Yoğurt", "Tereyağı"),
            instructions = listOf(
                "Döner dilimlenir",
                "Pide doğranır",
                "Sos hazırlanır",
                "Servis yapılır"
            ),
            imageUrl = "https://example.com/iskender.jpg",
            isFavorite = false
        )
    ))

    private val _filteredRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val filteredRecipes: StateFlow<List<Recipe>> = _filteredRecipes.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    init {
        updateFilteredRecipes()
        updateFavoriteRecipes()
    }

    fun setCategory(category: RecipeCategory?) {
        _selectedCategory.value = category
        updateFilteredRecipes()
    }

    private fun updateFilteredRecipes() {
        _filteredRecipes.value = _recipes.value.filter { recipe ->
            _selectedCategory.value?.let { category ->
                recipe.category == category.name
            } ?: true
        }
    }

    fun updateSearchQuery(query: String) {
        _searchResults.value = _recipes.value.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true) ||
            recipe.ingredients.any { it.contains(query, ignoreCase = true) }
        }
    }

    fun toggleFavorite(recipeId: String) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == recipeId) recipe.copy(isFavorite = !recipe.isFavorite)
            else recipe
        }
        updateFilteredRecipes()
        updateFavoriteRecipes()
    }

    private fun updateFavoriteRecipes() {
        _favoriteRecipes.value = _recipes.value.filter { it.isFavorite }
    }
} 