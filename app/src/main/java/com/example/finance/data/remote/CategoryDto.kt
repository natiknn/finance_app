package com.example.finance.data.remote
data class CategoryDto(
    val categoryId: Int? = null,
    val userId: Int,
    val name: String,
    val type: String,
    val colorHex: String? = null
)