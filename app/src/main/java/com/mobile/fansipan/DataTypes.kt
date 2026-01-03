package com.mobile.fansipan

import kotlinx.serialization.Serializable


data class Page(
    val start: Int,
    val end: Int)

@Serializable
data class TimeInPage(
    val page: Int,
    val time: Int)

@Serializable
data class ReadingRecord(
    val reader: String,
    val startTime: Long,
    val document: String,
    val allottedTime: Int,
    val pages: List<Int>,
    val timeInPages: List<TimeInPage>
)