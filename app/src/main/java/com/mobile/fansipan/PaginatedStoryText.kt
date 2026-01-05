package com.mobile.fansipan

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Helper function to find word boundaries using BreakIterator

fun drawRectangle(textLayoutResult: TextLayoutResult?, wordRange: IntRange): Rect {
    // Draw a rectangle over the calculated bounds
    var rect = Rect(Offset(0F, 0F), Size(0F,0F))
    if (textLayoutResult != null) {
        val startBounds = textLayoutResult.getBoundingBox(wordRange.first)
        val endBounds = textLayoutResult.getBoundingBox(wordRange.last - 1)
        rect = Rect(
            startBounds.left,
            startBounds.top,
            endBounds.right,
            endBounds.bottom
        )
    }
    return rect
}
@Composable
fun OverlayMessageDialog(
    showDialog: MutableState<Boolean>,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Analytics") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun PaginatedStoryText(storyText: String, initialTimeSeconds: Int,
                       ) {
    val scope = rememberCoroutineScope()

    //var maxChar by remember { mutableIntStateOf }
    val pages = remember { mutableStateListOf(0) }
    val ends = remember { mutableStateListOf(0) }

    // Disable swipe
    var userSwipeEnabled by remember { mutableStateOf(true) }
    // State to hold the number of pages needed, defaulting to 1
    var pageCount by remember { mutableIntStateOf(1) }
    // State to hold the start indices of each page


    // pagerState keeps the pages. Crucially, it keeps the "current" (visible) page
    val pagerState = rememberPagerState(pageCount = { pageCount })


    // overlay
    val showOverlay = remember { mutableStateOf(false) }
    val overlayMessage = remember { mutableStateOf("") }
    // timer
    var isRunning by remember { mutableStateOf(true) }
    var timeLeft by remember { mutableIntStateOf(initialTimeSeconds) }
    var pageReading by remember { mutableIntStateOf(0) }
    var timeInPage by remember { mutableIntStateOf(0) }

    //
    var startTime by remember { mutableStateOf<Long>(0)}
    val updateStartTime = fun (l: Long){
        startTime = l
    }
    val onWordTapped = fun (word: String, storyText: String, range: IntRange){
        val currentPage = pagerState.currentPage
        val numberOfWords = 0 // getNumberOfWords(storyText, storyText.length)
        //val currentPageStarts = if (pagerState.currentPage > 0) pages[pagerState.currentPage] else 0
        val numberOfWordsRead = 0 //getNumberOfWords(storyText, pages[currentPage] + range.first)
        overlayMessage.value =
            "Tapped on word: $word\n" +
                    "Total number of words: $numberOfWords\n" +
                    "Number of words read: $numberOfWordsRead\n" +
                    "CurrentPage: $currentPage"
        showOverlay.value = true
        }

    //
    var highlightedWordRange by remember {mutableStateOf<IntRange>(IntRange(0, 0))}
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = { offset ->
                if (timeLeft <= 1){
                textLayoutResultState.value?.let { textLayoutResult ->
                    val currentPage = pagerState.currentPage
                    val position = textLayoutResult.getOffsetForPosition(offset)
                    Log.d("FANSIPAN", "POSITION: $position")
                    Log.d("FANSIPAN", "PAGE STARTS " + pages[currentPage])
                    Log.d("FANSIPAN", "PAGE ENDS " + ends[currentPage])
                    //val wordRange =
                    //    getWordRange(storyText.substring(pages[currentPage]),
                    //        position)
                    val wordRange =
                     getWordRange(storyText,
                         position + pages[currentPage])
                    val tappedWord = storyText
                        .substring(wordRange.first, wordRange.last)
                    if (tappedWord.isNotBlank()) {
                        onWordTapped(tappedWord, storyText, wordRange)
                    }
                }
                }
            }
        )
    }


    // metrics
    val metrics = remember { mutableListOf<TimeInPage>() }

   // val readingRecord : ReadingRecord by remember { mutableStateOf(
   //     ReadingRecord(
   //         reader = "",
   //         timestamp = -1,
   //         document = "",
   //         allottedTime = -1,
   //         pages = -1,
   //         timeInPages = 
   //     )
   // )}
   
    // message
    var message by remember { mutableStateOf("") }
    

    val changeUserScrollEnabled = fun(value: Boolean) {
        userSwipeEnabled = value
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
        highlightedWordRange = IntRange(0, 0)
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
        if (pages.isNotEmpty()) {
            for (index in 0..<pages.size) {
                if (index < (pages.size - 1)){
                    pagesChars.add(ends[index] - pages[index])
                } else {
                    pagesChars.add(storyText.length - pages[index])
                }
            }
        }
        var pagesString =
            pagesChars.foldIndexed("") { index, accumulator, pageChar ->
                accumulator + ("[p" + (index + 1).toString()  + " : "
                        + pageChar.toString() + "ch" + "], ")
            }
        if (pagesString.isNotEmpty()) {
            pagesString = pagesString.dropLast(2)
        }
        var result =
            metrics.fold("") { accumulator, timeInPage ->
                accumulator + ("[p" + (timeInPage.page + 1).toString() + " : "
                        + timeInPage.time.toString() + "s], ")
            }

        if (result.isNotEmpty()) {
            result = result.dropLast(2)
        }
        //overlayMessage.value =
        //    "Chars per page:" + "\n" + pages + "\n" + "Reading sequence:" + "\n" + result

        val readingRecord =
            ReadingRecord(
                reader = "user1",
                startTime = startTime,
                document = "test1",
                allottedTime = initialTimeSeconds,
                pages = pages,
                timeInPages = metrics
            )

        scope.launch {
                try {
                    val response = service1.invokeLambda(readingRecord)
                    // Handle the response
                } catch (e: Exception) {
                    // Handle errors
                }
            }


    }

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            if (initialTimeSeconds != timeLeft) {
                var timeInPage: Int
                if (metrics.isEmpty()) {
                    timeInPage = (initialTimeSeconds - timeLeft)
                } else {
                    val currentReadingTime =
                        metrics.fold(0) { accumulator, timeInPage -> accumulator + timeInPage.time }

                    timeInPage =
                        (initialTimeSeconds
                                - (currentReadingTime
                                + timeLeft))
                }
                metrics.add(
                    TimeInPage(
                        page = pageReading,
                        time = timeInPage
                    )
                )
                pageReading = page
                //Log.d("FANSIPAN", metrics.toString())
            }
        }
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OverlayMessageDialog(
            showDialog = showOverlay,
            message = overlayMessage.value,
            onDismiss = { showOverlay.value = false }
        )


        HorizontalPager(
            state = pagerState,
            userScrollEnabled = userSwipeEnabled, // This disables user swiping
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
                if (ends[pageIndex] != 0) {
                    original = storyText.substring(start, ends[pageIndex])
                }

                Text(
                    text = original,
                    overflow = TextOverflow.Ellipsis,
                    modifier =
                        Modifier.align(Alignment.TopStart)
                        .then(pressIndicator)
                           .drawWithContent {
                                drawContent()
                                //
                        //        if (highlightedWordRange.last > 0) {
                        //            Log.d("FANSIPAN", highlightedWordRange.toString())
                        //            val rect =
                        //                drawRectangle(textLayoutResultState.value, highlightedWordRange)

//
//                                    drawRect(
//                                        color = Color.Yellow.copy(alpha = 0.5f), // Semi-transparent for visibility
//                                        topLeft = rect.topLeft,
//                                        size = rect.size
//                                    )
//                              }
                           },

                    onTextLayout = {
                        textLayoutResult ->
                        // detect tapping

                        textLayoutResultState.value = textLayoutResult
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
                        if (!pages.contains(start + endPageChar)) {
                            if ((start + endPageChar) < storyText.length) {
                                if (ends[pageIndex] == 0) {
                                    ends[pageIndex] = (start + endPageChar)
                                }
                                pages.add(start + endPageChar)
                                ends.add(0)
                                pageCount = pages.size
                                //Log.d("FANSIPAN", "START: " + pages.toString())
                                //Log.d("FANSIPAN", "END: " + ends.toString())
                            }
                        }

                    }
                )
            }
        }
       // Text(text = message)


        TimerCompose(
            updateMessage = updateMessage,
            userScrollEnabled = changeUserScrollEnabled,
            resetPagerState = resetPagerState,
            updateTimeLeft = updateTimeLeft,
            resetTimer = resetTimer,
            readTimeLeft = readTimeLeft,
            updateRunning = updateRunning,
            readRunning = readRunning,
            whenFinished = whenFinished,
            updateStartTime = updateStartTime
        )



//        Spacer(modifier = Modifier.height(16.dp))

    }
}
