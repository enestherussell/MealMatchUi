package com.example.mealmatchui.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val ingredients: List<String> = emptyList(),
    val preferences: List<String> = emptyList()
) 