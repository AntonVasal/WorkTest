package com.example.test.ui.usersFragment.usersViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.core.baseEvent.Event
import com.example.test.core.baseStates.BaseStates
import com.example.test.data.repositories.UsersRepository
import com.example.test.domain.mappers.realmToModel
import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.UserRealmObject
import com.example.test.utils.extensions.newEvent
import com.example.test.utils.extensions.newValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    ViewModel() {

    private val _state = MutableLiveData<Event<BaseStates>>()
    val state: LiveData<Event<BaseStates>> = _state

    private val _users = MutableLiveData<ArrayList<UserModel>>()
    val users: LiveData<ArrayList<UserModel>> = _users

    init {
        getUsersWirthRealm()
    }

    fun getUsersWithRetrofit(isEmpty: Boolean) {
        if (isEmpty){
            _state.newEvent(BaseStates.LoadingState)
        }
        viewModelScope.launch {
            runCatching {
                usersRepository.getUsersWithRetrofit()
            }.onSuccess {
                usersRepository.setUsersToRealm(it)
                _state.newEvent(BaseStates.SuccessState)
                _users.newValue(it)
            }.onFailure {
                _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
            }
        }
    }

    private fun getUsersWirthRealm() {
        runCatching {
            usersRepository.getUsersWithRealm()
        }.onSuccess {
            workWithRealmList(it)
        }.onFailure {
            _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
        }
    }

    private fun workWithRealmList(list: List<UserRealmObject>) {
        var isEmpty = true
        if(list.isNotEmpty()){
            val it = ArrayList<UserModel>()
            for (obj in list) {
                it.add(realmToModel(obj))
            }
            _users.newValue(it)
            isEmpty = false
        }
        getUsersWithRetrofit(isEmpty)
    }

    fun getUserReposWithRealm(login:String,id:Int){
        runCatching {

        }.onSuccess {

        }.onFailure {
            _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
        }
    }

}
