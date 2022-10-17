package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login

import android.net.Uri
import androidx.annotation.StringRes


sealed class LoginState {

    object Uninitialized: LoginState()

    object Loading: LoginState()

    data class Login(
        val idToken: String
    ): LoginState()

    sealed class Success: LoginState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
        ): Success()

        object NotRegistered: Success()

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): LoginState()
}