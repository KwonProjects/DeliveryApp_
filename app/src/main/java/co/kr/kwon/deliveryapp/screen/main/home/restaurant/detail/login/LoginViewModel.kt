package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.OrderEntity
import co.kr.kwon.deliveryapp.data.preference.AppPreferenceManager
import co.kr.kwon.deliveryapp.data.repository.order.Result
import co.kr.kwon.deliveryapp.data.repository.user.UserRepository
import co.kr.kwon.deliveryapp.model.restaurant.order.OrderHistoryModel
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import co.kr.kwon.deliveryapp.screen.main.my.MyState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val appPreferenceManager: AppPreferenceManager
) : BaseViewModel() {

    val loginStateLiveData = MutableLiveData<LoginState>(LoginState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        loginStateLiveData.value = LoginState.Loading
        appPreferenceManager.getIdToken()?.let {
            Log.e("MyViewModel", it.toString())
            loginStateLiveData.value = LoginState.Login(it)
        } ?: kotlin.run {
            loginStateLiveData.value = LoginState.Success.NotRegistered
        }
    }

    fun saveToken(IdToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(IdToken)
            fetchData()
        }
    }

    fun setUserInfo(firebase: FirebaseUser?) = viewModelScope.launch {
        firebase?.let { user ->
            loginStateLiveData.value = LoginState.Success.Registered(
                userName = user.displayName ?: "익명",
                profileImageUri = user.photoUrl
            )
        } ?: kotlin.run {
            loginStateLiveData.value = LoginState.Success.NotRegistered
        }
    }
}
