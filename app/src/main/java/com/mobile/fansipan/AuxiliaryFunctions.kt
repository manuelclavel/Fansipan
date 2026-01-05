package com.mobile.fansipan

import android.util.Log
import androidx.compose.ui.text.TextLayoutResult
import java.text.BreakIterator
import java.util.Locale

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

//
fun getNumberOfWords(text: String, end: Int): Int {
    val breakIterator = BreakIterator.getWordInstance(Locale.getDefault())
    breakIterator.setText(text)
    var numberOfWords = 0

    var index = 0;
    while (index < (end - 1)){
        while (Character.isWhitespace(text[index])) {
            index++
        }
        while (index < (end - 1) && !Character.isWhitespace(text[index])) {
            index++
        }
        numberOfWords++
    }
    return numberOfWords
}
fun getWordRange(text: String, offset: Int): IntRange {
    Log.d("FANSIPAN",  "CHARACTER: " + text.substring(offset, offset + 1))

    //val breakIterator = BreakIterator.getWordInstance(Locale.US)
    //breakIterator.setText(text)
    // Find the word boundary that contains the tapped offset
    //var start = breakIterator.preceding(offset)
    var start = offset
    //if (start == BreakIterator.DONE) start = 0
    //var end = breakIterator.following(offset)
    //if (end == BreakIterator.DONE) end = text.length

    // Adjust boundaries to capture the full word and ignore surrounding whitespace
    while (start > 0 && !Character.isWhitespace(text[start])) {
        start--
    }
    var end = offset
    while (end < text.length && !Character.isWhitespace(text[end])) {
      end++
    }
    Log.d("FANSIPAN", "RANGE: " + IntRange(start, end).toString())
    return IntRange(start, end)
}
fun calculatePageBreaks(
    storyText: String,
    start: Int,
    textLayoutResult: TextLayoutResult
): Int {
    // Find the last visible line on the first layout pass
    val lastVisibleLineIndex = textLayoutResult.lineCount - 1

    // Find the end character index of that last visible line
    val lastVisibleCharIndex =
        textLayoutResult.getLineEnd(lastVisibleLineIndex, visibleEnd = true)

    //
    //val validStart = textLayoutResult.getLineStart(lastVisibleLineIndex)
    var validEnd = start + lastVisibleCharIndex
    var removedChars = ""
    if (validEnd < storyText.length) {
        var found = false
        while (!found) {
            if (!storyText.get(validEnd).isWhitespace()) {
                removedChars = storyText[validEnd].toString() + removedChars
                validEnd--
            } else {
                found = true
            }
        }
        Log.d("FANSIPAN", "START: " + start + " | " + "REMOVED: " + removedChars)
        return lastVisibleCharIndex - removedChars.length + 1
    } else {
        Log.d(
            "FANSIPAN",
            "START: " + start + " | " + "REMAINDER: " + (storyText.length - start).toString()
        )
        return storyText.length - start
    }
    //Log.d("FANSIPAN", "ORIGINAL: "  + storyText.substring(validStart, validEnd) )
}
