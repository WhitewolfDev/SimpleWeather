package com.jggdevelopment.simpleweather.internal

import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async {
            block.invoke(this)
        }
    }
}