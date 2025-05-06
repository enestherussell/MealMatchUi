package com.example.mealmatchui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmatchui.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun register(email: String, password: String, name: String) {
        // Şimdilik boş bırakıyoruz
    }

    fun login(email: String, password: String) {
        // Şimdilik boş bırakıyoruz
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    fun updateUserIngredients(ingredients: List<String>) {
        viewModelScope.launch {
            _currentUser.value = _currentUser.value?.copy(ingredients = ingredients)
        }
    }

    fun updateUserPreferences(preferences: List<String>) {
        viewModelScope.launch {
            _currentUser.value = _currentUser.value?.copy(preferences = preferences)
        }
    }
} 