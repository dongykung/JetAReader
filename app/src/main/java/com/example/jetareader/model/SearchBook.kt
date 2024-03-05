package com.example.jetareader.model

data class SearchBook(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)