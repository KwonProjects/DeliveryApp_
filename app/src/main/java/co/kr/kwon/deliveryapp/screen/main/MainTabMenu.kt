package co.kr.kwon.deliveryapp.screen.main

import androidx.annotation.IdRes
import co.kr.kwon.deliveryapp.R

enum class MainTabMenu(@IdRes val menuId: Int) {
    HOME(R.id.menu_home), LIKE(R.id.menu_like), MY(R.id.menu_my)
}
