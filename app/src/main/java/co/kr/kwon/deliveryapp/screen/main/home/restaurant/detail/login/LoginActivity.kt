package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.ActivityLoginBinding
import co.kr.kwon.deliveryapp.extensions.isGone
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.extensions.load
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.my.MyFragment
import co.kr.kwon.deliveryapp.screen.main.my.MyState
import co.kr.kwon.deliveryapp.util.event.MenuChangeEventBus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {


    override val viewModel by viewModel<LoginViewModel>()

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(this@LoginActivity, gso) }


    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        Log.e(MyFragment.TAG, "firebaseAuthWithGoogle: ${account.id}")
                        viewModel.saveToken(account.idToken ?: throw Exception())
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }

    override fun initViews() = with(binding) {
        loginBtn.setOnClickListener {
            signInGoogle()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible(true)
        loginRequiredGroup.isGone(true)
    }

    private fun handleLoginState(state: LoginState.Login) = with(binding) {
        progressBar.isVisible(true)
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleSuccessState(state: LoginState.Success) = with(binding) {
        progressBar.isGone(true)
        when (state) {
            is LoginState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is LoginState.Success.NotRegistered -> {

                loginRequiredGroup.isVisible(true)
            }
        }
    }

    private fun handleRegisteredState(state: LoginState.Success.Registered) = with(binding) {
        loginRequiredGroup.isGone(true)
        finish()
    }

    private fun handleErrorState(state: LoginState.Error) {
        Toast.makeText(this@LoginActivity, state.messageId, Toast.LENGTH_SHORT).show()
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }


    override fun observeData() = viewModel.loginStateLiveData.observe(this) {
        when(it){
            is LoginState.Uninitialized -> initViews()
            is LoginState.Loading -> handleLoadingState()
            is LoginState.Login -> handleLoginState(it)
            is LoginState.Success -> handleSuccessState(it)
            else -> {}
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, LoginActivity::class.java)
    }
}