package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail.logout

import androidx.lifecycle.viewModelScope
import co.kr.kwon.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyInfoViewModel : BaseViewModel() {

    override fun fetchData(): Job  = viewModelScope.launch{

    }
}