package com.mobile.fansipan

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch





private fun calculatePageBreaks(
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
        Log.d("FANSIPAN", "START: " + start + " | " + "REMAINDER: " + (storyText.length - start).toString())
        return storyText.length - start
    }
    //Log.d("FANSIPAN", "ORIGINAL: "  + storyText.substring(validStart, validEnd) )
}


@Composable
fun PaginatedStoryText(storyText: String, initialTimeSeconds: Int) {

    //var maxChar by remember { mutableIntStateOf }
    val pages = remember { mutableStateListOf(0) }
    val ends = remember { mutableStateListOf(0) }

    // message
    var message by remember { mutableStateOf("") }
    // metrics
    val metrics = remember { mutableListOf<TimeInPage>() }

    // timer
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(initialTimeSeconds) }
    var pageReading by remember { mutableIntStateOf(0) }
    var timeInPage by remember { mutableIntStateOf(0) }

    // Disable swipe
    var userScrollEnabled by remember { mutableStateOf(false) }
    // State to hold the number of pages needed, defaulting to 1
    var pageCount by remember { mutableIntStateOf(1) }
    // State to hold the start indices of each page

    val pagerState = rememberPagerState(pageCount = { pageCount })

    val changeUserScrollEnabled = fun(value: Boolean) {
        userScrollEnabled = value
    }
    val coroutineScope = rememberCoroutineScope()


    val updateMessage = fun(text: String) {
        message = text
    }
    val resetPagerState = fun() {
        coroutineScope.launch {
            pagerState.scrollToPage(0) // or pagerState.scrollToPage(0) for instant jump
        }
    }

    val readRunning = fun(): Boolean {
        return isRunning
    }
    val updateRunning = fun(b: Boolean) {
        isRunning = b
    }

    val readTimeLeft = fun(): Int {
        return timeLeft
    }
    val updateTimeLeft = fun() {
        timeLeft--
    }
    val resetTimer = fun() {
        timeLeft = initialTimeSeconds
        pageReading = 0
        metrics.clear()

    }
    val whenFinished = fun() {
        if (metrics.isEmpty()) {
            timeInPage = initialTimeSeconds
        } else {
            val currentReadingTime =
                metrics.fold(0) { accumulator, timeInPage -> accumulator + timeInPage.time }

            timeInPage =
                (initialTimeSeconds
                        - currentReadingTime)
        }
        metrics.add(
            TimeInPage(
                page = pageReading,
                time = timeInPage
            )
        )

        val pagesChars = mutableListOf<Int>()
        // if (pages.isNotEmpty()) {
        //     for (index in 0..<pages.size) {
        //if (index > 0) {
        //         if (index < pages.size - 1) {
        //pagesChars.add(pages[index + 1] - pageStarts[index])

        //         } else {
        //pagesChars.add(totalChars - pageStarts[index])
        //        }
        //}
        //    }
        //}
        // var pages =
        //     pagesChars.fold("") { accumulator, pageStart ->
        //         "$accumulator$pageStart,"
        //     }
        //if (pages.isNotEmpty()) {
        //    pages = pages.dropLast(1)
        // }
        // var result =
        //     metrics.fold("") { accumulator, timeInPage ->
        //         accumulator + ("[p" + (timeInPage.page + 1).toString() + " : "
        //                + timeInPage.time.toString() + "s], ")
        //    }

        // if (result.isNotEmpty()) {
        //     result = result.dropLast(2)
        // }


    }

    // LaunchedEffect(pagerState) {
    // Collect from the a snapshotFlow reading the currentPage
    //     snapshotFlow { pagerState.currentPage }.collect { page ->
    // Do something with each page change, for example:
    //         if (initialTimeSeconds != timeLeft) {
    //             var timeInPage: Int
    //             if (metrics.isEmpty()) {
    //                 timeInPage = (initialTimeSeconds - timeLeft)
    //            } else {
    //                val currentReadingTime =
    //                    metrics.fold(0) { accumulator, timeInPage -> accumulator + timeInPage.time }

    //                 timeInPage =
    //                     (initialTimeSeconds
    //                             - (currentReadingTime
    //                             + timeLeft))
    //             }
    //             metrics.add(
    //                 TimeInPage(
    //                     page = pageReading,
    //                     time = timeInPage
    //                 )
    //            )

    //       pageReading = page
    //Log.d("FANSIPAN", metrics.toString())
    // }

    //  }
    //}

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)

        TimerCompose(
            updateMessage = updateMessage,
            userScrollEnabled = changeUserScrollEnabled,
            resetPagerState = resetPagerState,
            updateTimeLeft = updateTimeLeft,
            resetTimer = resetTimer,
            readTimeLeft = readTimeLeft,
            updateRunning = updateRunning,
            readRunning = readRunning,
            whenFinished = whenFinished
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = userScrollEnabled, // This disables user swiping
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            // A Box to constrain the Text size for measurement
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                val start = pages[pageIndex]
                var original = storyText.substring(start)
                if (ends[pageIndex] != 0){
                    original = storyText.substring(start, ends[pageIndex])
                }
                //Log.d("FANSIPAN", start.toString())
                //var end by remember {mutableIntStateOf(storyText.length)}

                Text(
                    //text = storyText.substring(pages[pageIndex]),
                    text = original,
                    overflow = TextOverflow.Ellipsis,
                    //maxLines = 24,
                    modifier = Modifier.align(Alignment.TopStart),
                    onTextLayout = { textLayoutResult ->
                        // This callback runs when the text is laid out.
                        //The onTextLayout callback in HorizontalPager
                        //is only triggered when a page is initially
                        //composed and laid out for the first time,
                        //or when its content changes in a way that requires
                        //a new layout calculation (e.g., text content or constraints change).
                        //This is due to the lazy nature of HorizontalPager.
                        //Pages that are already in the composition and just being scrolled
                        //back into view are not recomposed from scratch,
                        //so their onTextLayout callback (which fires during the layout phase) is not re-invoked automatically.

                        val endPageChar = calculatePageBreaks(storyText, start, textLayoutResult)
                        //if (ends[pageIndex] == 0) {
                        //    ends[pageIndex] = (start + endPageChar)
                            // end = calculatePageBreaks(storyText, textLayoutResult)
                            if (!pages.contains(start + endPageChar)) {
                                if ((start + endPageChar) < storyText.length) {
                                    if (ends[pageIndex] == 0) {
                                        ends[pageIndex] = (start + endPageChar)
                                    }
                                    pages.add(start + endPageChar)
                                    ends.add(0)
                                    pageCount = pages.size
                                    Log.d("FANSIPAN", "START: " + pages.toString())
                                    Log.d("FANSIPAN", "END: " + ends.toString())
                                }
                            }
                        //} else {
                            //Log.d("FANSIPAN", "END: " + ends.toString())
                        //}
                    }
                )
            }
        }
    }
}
