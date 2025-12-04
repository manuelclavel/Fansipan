package com.mobile.fansipan

fun paginateText(text: String, charsPerPage: Int): List<String> {
    val pages = mutableListOf<String>()
    var currentIndex = 0
    while (currentIndex < text.length) {
        // Determine the end index, ensuring it doesn't exceed text length
        val endIndex = (currentIndex + charsPerPage).coerceAtMost(text.length)
        // Extract the substring
        pages.add(text.substring(currentIndex, endIndex))
        // Move the index for the next page
        currentIndex = endIndex
    }
    return pages
}