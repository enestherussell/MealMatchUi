package com.example.mealmatchui.data.repository

import com.example.mealmatchui.data.model.Recipe
import com.example.mealmatchui.data.model.RecipeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipeRepository {
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
     val allRecipes: StateFlow<List<Recipe>> = _allRecipes.asStateFlow()

    private val _filteredRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val filteredRecipes: StateFlow<List<Recipe>> = _filteredRecipes.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    init {
        _allRecipes.value = getRecipes()
        _filteredRecipes.value = _allRecipes.value
    }

    fun filterByCategory(category: RecipeCategory?) {
        _filteredRecipes.update { _ ->
            when (category) {
                null, RecipeCategory.ALL -> _allRecipes.value
                else -> _allRecipes.value.filter { it.category == category.name }
            }
        }
    }

    fun searchRecipes(query: String, ingredients: List<String>) {
        _searchResults.update { recipes ->
            recipes.filter { recipe ->
                val matchesQuery = query.isEmpty() || 
                    recipe.name.contains(query, ignoreCase = true) ||
                    recipe.ingredients.any { it.contains(query, ignoreCase = true) }
                
                val matchesIngredients = ingredients.isEmpty() ||
                    ingredients.all { ingredient ->
                        recipe.ingredients.any { it.contains(ingredient, ignoreCase = true) }
                    }
                
                matchesQuery && matchesIngredients
            }
        }
    }

    fun getRecipeById(id: String): Recipe? {
        return _allRecipes.value.find { it.id == id }
    }

    fun toggleFavorite(recipeId: String) {
        val recipe = getRecipeById(recipeId) ?: return
        
        _allRecipes.update { recipes ->
            recipes.map { 
                if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite)
                else it
            }
        }
        
        _filteredRecipes.update { recipes ->
            recipes.map { 
                if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite)
                else it
            }
        }
        
        _searchResults.update { recipes ->
            recipes.map { 
                if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite)
                else it
            }
        }
        
        _favoriteRecipes.update { recipes ->
            if (recipe.isFavorite) {
                recipes.filter { it.id != recipeId }
            } else {
                recipes + recipe.copy(isFavorite = true)
            }
        }
    }

    fun getRecipes(): List<Recipe> {
        return listOf(
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
                isFavorite = false
            ),
            Recipe(
                id = "3",
                name = "Baklava",
                category = RecipeCategory.DESSERT.name,
                ingredients = listOf("Un", "Yağ", "Şeker", "Antep Fıstığı", "Şerbet"),
                instructions = listOf(
                    "Hamur açılır",
                    "Katlar hazırlanır",
                    "Fıstık serpilir",
                    "Pişirilir",
                    "Şerbet dökülür"
                ),
                imageUrl = "https://example.com/baklava.jpg",
                isFavorite = false
            )
        )
    }
} 