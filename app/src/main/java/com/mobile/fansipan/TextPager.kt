package com.mobile.fansipan

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextPager(
    pages: List<String>,
    modifier: Modifier
) {
    val pagerState = rememberPagerState(pageCount = {
        // The amount of pages this Pager will have.
        //pages.size
       3
    })


    //LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
    //    snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            // viewModel.sendPageSelectedEvent(page)
    //        Log.d("FANSIPAN", "Page changed to $page")
    //    }
    //}

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text(
                text = "Page $page",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxSize(),
                color = Color.Black // Adjust text color as needed
            )
        }
    }
}

//You can use the snapshotFlow function to observe changes
// to these variables and react to them. For example, to send an analytics event
//on each page change, you can do the following: