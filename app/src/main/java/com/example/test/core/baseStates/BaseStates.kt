package com.example.test.core.baseStates

sealed class BaseStates : BaseState() {
    object LoadingState : BaseStates()
    object SuccessState : BaseStates()
    data class ErrorState(val message: String) : BaseStates()
}