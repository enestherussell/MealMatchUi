package com.example.mealmatchui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealmatchui.data.model.RecipeCategory
import com.example.mealmatchui.ui.components.RecipeCard
import com.example.mealmatchui.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    viewModel: RecipeViewModel = viewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredRecipes by viewModel.filteredRecipes.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MealMatch") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Kategori Seçimi
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(RecipeCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.setCategory(category) },
                            label = { Text(category.toDisplayName()) }
                        )
                    }
                }
            }

            // Arama Çubuğu
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = onSearchClick
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Malzeme ara...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ara") },
                        readOnly = true,
                        enabled = false
                    )
                }
            }

            // Yemek Bul Butonu
            item {
                Button(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Text("Yemek Bul")
                }
            }

            // Önerilen Tarifler Başlığı
            item {
                Text(
                    text = "Önerilen Tarifler",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Önerilen Tarifler Listesi
            items(filteredRecipes) { recipe ->
                RecipeCard(
                    recipeName = recipe.name,
                    isFavorite = recipe.isFavorite,
                    onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }
    }
} 