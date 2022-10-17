package co.kr.kwon.deliveryapp.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import co.kr.kwon.deliveryapp.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)
}