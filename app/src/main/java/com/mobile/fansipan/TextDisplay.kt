package com.mobile.fansipan

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TextDisplay(text: String,
                setLastVisibleCharIndex: (Int) ->Unit) {
    Column()
    {
        Text(
            text = text,
            maxLines = 10, // Initial number of lines
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowHeight || textLayoutResult.didOverflowWidth) {
                    // Determine the last visible line index
                    val lastLineIndex = textLayoutResult.lineCount - 1

                    // Get the end index of the last visible character on that line
                    // visibleEnd = true
                    // ensures we get the index of the last character that is actually displayed
                    setLastVisibleCharIndex(textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) - 1)
                    // Subtract 1 because getLineEnd returns the index *after* the last character
                } else {
                    // If no overflow, all characters are visible
                    setLastVisibleCharIndex(text.length - 1)
                }
            }
        )
    }
}