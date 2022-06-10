package com.example.test.core.baseViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.core.baseEvent.Event
import com.example.test.core.baseStates.BaseState


open class BaseViewModel<State : BaseState> : ViewModel() {
    val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state
}