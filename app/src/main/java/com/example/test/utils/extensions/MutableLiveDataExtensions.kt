package com.example.test.utils.extensions

import androidx.lifecycle.MutableLiveData
import com.example.test.core.baseEvent.Event

fun <T> MutableLiveData<T>.newValue(newValue: T) {
    postValue(newValue)
}

fun <T> MutableLiveData<Event<T>>.newEvent(newEvent: T) {
    postValue(Event(newEvent))
}