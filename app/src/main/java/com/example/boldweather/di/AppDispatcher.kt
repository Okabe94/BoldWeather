package com.example.boldweather.di

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatcher {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}