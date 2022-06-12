package com.example.test.ui.usersFragment.usersViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.core.baseStates.BaseStates
import com.example.test.core.baseViewModel.BaseViewModel
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
    BaseViewModel<BaseStates>() {

    private val _users = MutableLiveData<ArrayList<UserModel>>()
    val users: LiveData<ArrayList<UserModel>> = _users

    init {
        getUsersWithRealm(true)
    }

    private fun getUsersWithRetrofit(isEmpty: Boolean) {
        if (isEmpty) {
            _state.newEvent(BaseStates.LoadingState)
        }
        viewModelScope.launch {
            runCatching {
                usersRepository.getUsersWithRetrofit()
            }.onSuccess {
                if (!isEmpty) {
                    users.value?.forEach { model ->
                        it.find { obj -> obj.id == model.id }?.changesCount = model.changesCount
                    }
                }
                usersRepository.setUsersToRealm(it)
                _state.newEvent(BaseStates.SuccessState)
                _users.newValue(it)
            }.onFailure {
                _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
            }
        }
    }

    private fun getUsersWithRealm(isNeedRetrofitUpdate: Boolean) {
        runCatching {
            usersRepository.getUsersWithRealm()
        }.onSuccess {
            workWithRealmList(it, isNeedRetrofitUpdate)
        }.onFailure {
            _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
        }
    }

    private fun workWithRealmList(list: List<UserRealmObject>, isNeedRetrofitUpdate: Boolean) {
        var isEmpty = true
        if (list.isNotEmpty()) {
            val it = ArrayList<UserModel>()
            for (obj in list) {
                it.add(realmToModel(obj))
            }
            _users.newValue(it)
            isEmpty = false
        }
        if (isNeedRetrofitUpdate) {
            getUsersWithRetrofit(isEmpty)
        }
    }

    fun updateUserInRealm(id: Int, changesCount: Int) {
        runCatching {
            usersRepository.updateUserInRealm(id,changesCount)
        }.onSuccess {
            getUsersWithRealm(false)
        }
    }


}
