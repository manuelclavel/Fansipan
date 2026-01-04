package com.mobile.fansipan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun TimerCompose(
    userScrollEnabled: (Boolean) -> Unit,
    resetPagerState: () -> Unit,
    updateTimeLeft: () -> Unit,
    resetTimer: () -> Unit,
    readTimeLeft: () -> Int,
    readRunning: () -> Boolean,
    updateRunning: (Boolean) -> Unit,
    whenFinished: () -> Unit,
    updateMessage: (String) -> Unit,
    updateStartTime: (Long) -> Unit
) {


    LaunchedEffect(key1 = readRunning()) {
        userScrollEnabled(readRunning())
        if (readRunning()) {
            while (readTimeLeft() > 0) {
                delay(1000L) // Delay for 1 second
                updateTimeLeft()
            }
            updateRunning(false) // Stop the timer when it reaches 0
            whenFinished()
        }
    }
    Column(
        //modifier = Modifier
            //.fillMaxSize()
        //    .padding(16.dp),
        //horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {
        if (readTimeLeft() >= 0) {
            updateMessage("Time Left: " + readTimeLeft() + " seconds")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val currentTimestamp: Long = System.currentTimeMillis()
                updateStartTime(currentTimestamp)
                updateRunning(true)
                             }, enabled = !readRunning()
                    && readTimeLeft() >= 0) {
                Text("Start")
            }
            //Button(onClick = { isRunning = false }, enabled = isRunning) {
            //    Text("Pause")
            //}
            Button(onClick = {
                resetTimer()
                updateRunning(false)
                resetPagerState()
            }) {
                Text("Reset")
            }
        }
    }
}
