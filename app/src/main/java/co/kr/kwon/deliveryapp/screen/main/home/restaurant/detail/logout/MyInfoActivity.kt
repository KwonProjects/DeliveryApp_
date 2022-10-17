package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.logout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.ActivityMyInfoBinding
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.login.LoginActivity
import co.kr.kwon.deliveryapp.screen.main.my.MyFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MyInfoActivity : BaseActivity<MyInfoViewModel, ActivityMyInfoBinding>() {

    override val viewModel by viewModel<MyInfoViewModel>()

    override fun getViewBinding(): ActivityMyInfoBinding  = ActivityMyInfoBinding.inflate(layoutInflater)

    private fun alertLogout(afterAction: () -> Unit) {
        AlertDialog.Builder(this@MyInfoActivity)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.logout_alert_content))
            .setPositiveButton(getString(R.string.confirm_move)) { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun observeData() {

    }


    companion object {

        fun newIntent(context: Context) =
            Intent(context, MyInfoActivity::class.java)
    }
}