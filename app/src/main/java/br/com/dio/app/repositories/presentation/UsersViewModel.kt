package br.com.dio.app.repositories.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.app.repositories.data.model.User
import br.com.dio.app.repositories.domain.ListUsersUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UsersViewModel(
    private val listUsersUseCase: ListUsersUseCase
) : ViewModel() {

    private val _users = MutableLiveData<State>()
    val users: LiveData<State> = _users

    fun getUsersList(user: String) {
        viewModelScope.launch {
            listUsersUseCase(user)
                .onStart { _users.postValue(State.Loading) }
                .catch { _users.postValue(State.Error(it)) }
                .collect {
                    _users.postValue(State.Success(it))
                }

        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<User>) : State()
        data class Error(val error: Throwable) : State()
    }
}