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
        _allRecipes.value = generateSampleRecipes()
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

    private fun generateSampleRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = "1",
                name = "Köfte",
                category = RecipeCategory.MEAT.name,
                ingredients = listOf(
                    "Kıyma",
                    "Soğan",
                    "Sarımsak",
                    "Yumurta",
                    "Ekmek içi"
                ),
                instructions = listOf(
                    "Kıymayı geniş bir kaba alın",
                    "Soğan ve sarımsakları rendeleyin",
                    "Yumurta ve ekmek içini ekleyin",
                    "Tüm malzemeleri iyice yoğurun",
                    "Köfte şekli verip pişirin"
                ),
                imageUrl = "https://example.com/kofte.jpg",
                isFavorite = false
            ),
            Recipe(
                id = "2",
                name = "Mercimek Çorbası",
                category = RecipeCategory.VEGETABLE.name,
                ingredients = listOf(
                    "Kırmızı mercimek",
                    "Soğan",
                    "Havuç",
                    "Patates",
                    "Salça"
                ),
                instructions = listOf(
                    "Mercimekleri yıkayın",
                    "Sebzeleri doğrayın",
                    "Tencereye alıp kavurun",
                    "Su ekleyip pişirin",
                    "Blenderdan geçirin"
                ),
                imageUrl = "https://example.com/mercimek.jpg",
                isFavorite = false
            ),
            Recipe(
                id = "3",
                name = "Vegan Burger",
                category = RecipeCategory.VEGAN.name,
                ingredients = listOf(
                    "Mantar",
                    "Nohut",
                    "Soğan",
                    "Sarımsak",
                    "Ekmek"
                ),
                instructions = listOf(
                    "Mantarları doğrayın",
                    "Nohutları ezin",
                    "Malzemeleri karıştırın",
                    "Burger şekli verin",
                    "Pişirin"
                ),
                imageUrl = "https://example.com/vegan-burger.jpg",
                isFavorite = false
            ),
            Recipe(
                id = "4",
                name = "Çikolatalı Kek",
                category = RecipeCategory.DESSERT.name,
                ingredients = listOf(
                    "Un",
                    "Kakao",
                    "Yumurta",
                    "Süt",
                    "Şeker"
                ),
                instructions = listOf(
                    "Malzemeleri karıştırın",
                    "Kek kalıbına dökün",
                    "Fırında pişirin",
                    "Soğutun",
                    "Servis yapın"
                ),
                imageUrl = "https://example.com/chocolate-cake.jpg",
                isFavorite = false
            )
        )
    }
} 