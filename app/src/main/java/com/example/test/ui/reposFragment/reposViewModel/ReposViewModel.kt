package com.example.test.ui.reposFragment.reposViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.core.baseStates.BaseStates
import com.example.test.core.baseViewModel.BaseViewModel
import com.example.test.data.repositories.UsersRepository
import com.example.test.utils.extensions.newEvent
import com.example.test.utils.extensions.newValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(private val usersRepository: UsersRepository): BaseViewModel<BaseStates>() {

    private val _repos = MutableLiveData<ArrayList<String>>()
    val repos: LiveData<ArrayList<String>> = _repos

    fun getUserReposWithRealm(login:String,id:Int){
        runCatching {
            usersRepository.getReposWithRealm(id)
        }.onSuccess {
            workWithRealmReposList(ArrayList(it.orEmpty()),login,id)
        }.onFailure {
            _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
        }
    }

    private fun workWithRealmReposList(arrayList: ArrayList<String>, login: String, id: Int) {
        var isShowLoader = true
        if (arrayList.isNotEmpty()){
            _repos.newValue(arrayList)
            isShowLoader = false
        }
        getReposWithRetrofit(login,id,isShowLoader)
    }

    private fun getReposWithRetrofit(login: String, id: Int, isShowLoader: Boolean) {
        if (isShowLoader){
            _state.newEvent(BaseStates.LoadingState)
        }
        viewModelScope.launch {
            runCatching {
                usersRepository.getUsersReposWithRetrofit(login)
            }.onSuccess {
                val list = ArrayList<String>()
                for (model in it){
                    list.add(model.repo)
                }
                usersRepository.setReposToRealm(list,id)
                _state.newEvent(BaseStates.SuccessState)
                _repos.newValue(list)
            }.onFailure {
                _state.newEvent(BaseStates.ErrorState(it.stackTraceToString()))
            }
        }
    }
}