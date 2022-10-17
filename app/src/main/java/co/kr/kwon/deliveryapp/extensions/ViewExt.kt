package co.kr.kwon.deliveryapp.extensions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible


fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.isVisible(value: Boolean) {
    isVisible = value
}
fun View.isGone(value: Boolean){
    isGone = value
}