package com.example.mealmatchui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealmatchui.data.model.Recipe
import com.example.mealmatchui.ui.components.RecipeCard
import com.example.mealmatchui.ui.viewmodel.RecipeViewModel

@Composable
fun FavoritesScreen(
    onRecipeClick: (Recipe) -> Unit,
    viewModel: RecipeViewModel = viewModel()
) {
    val favorites by viewModel.favoriteRecipes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Favori Tariflerim",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "Henüz favori tarif eklenmemiş",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onRecipeClick = { onRecipeClick(recipe) },
                        onFavoriteClick = { viewModel.toggleFavorite(recipe) }
                    )
                }
            }
        }
    }
} 