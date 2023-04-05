package com.vinodpatildev.eventmaster.data.model

data class ApiResponse<out T : Any>(
    val data:T,
    val cookies:String,
    )
