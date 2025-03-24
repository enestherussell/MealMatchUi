package com.example.mealmatchui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmatchui.data.model.Recipe
import com.example.mealmatchui.data.model.RecipeCategory
import com.example.mealmatchui.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val repository = RecipeRepository()

    // State
    private val _selectedCategory = MutableStateFlow<RecipeCategory?>(RecipeCategory.ALL)
    val selectedCategory: StateFlow<RecipeCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedIngredients = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredients: StateFlow<List<String>> = _selectedIngredients.asStateFlow()

    // Derived State
    val recipes: StateFlow<List<Recipe>> = repository.allRecipes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredRecipes: StateFlow<List<Recipe>> = repository.filteredRecipes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchResults: StateFlow<List<Recipe>> = repository.searchResults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.favoriteRecipes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Actions
    fun setCategory(category: RecipeCategory?) {
        _selectedCategory.update { category }
        repository.filterByCategory(category)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
        repository.searchRecipes(query, _selectedIngredients.value)
    }

    fun addIngredient(ingredient: String) {
        _selectedIngredients.update { current ->
            if (current.contains(ingredient)) current else current + ingredient
        }
        repository.searchRecipes(_searchQuery.value, _selectedIngredients.value)
    }

    fun removeIngredient(ingredient: String) {
        _selectedIngredients.update { current ->
            current.filter { it != ingredient }
        }
        repository.searchRecipes(_searchQuery.value, _selectedIngredients.value)
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId)
        }
    }
} 