package co.kr.kwon.deliveryapp.screen.main

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.databinding.ActivityMainBinding
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.main.home.HomeFragment
import co.kr.kwon.deliveryapp.screen.main.like.RestaurantLikeListFragment
import co.kr.kwon.deliveryapp.screen.main.my.MyFragment
import co.kr.kwon.deliveryapp.screen.main.orderhistory.OrderHistoryListFragment
import co.kr.kwon.deliveryapp.util.event.MenuChangeEventBus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener {


    override val viewModel by viewModel<MainViewModel>()

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        bottomNav.setOnItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_like ->{
                showFragment(RestaurantLikeListFragment.newInstance(), RestaurantLikeListFragment.TAG)
                true
            }
            R.id.menu_order_list ->{
                showFragment(OrderHistoryListFragment.newInstance(), OrderHistoryListFragment.TAG)
                true
            }
            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }


    override fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }

    }
}