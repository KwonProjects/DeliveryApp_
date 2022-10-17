package co.kr.kwon.deliveryapp.util.date

import java.text.SimpleDateFormat
import java.util.*

fun ConvertDate(date: Long): String {
    return SimpleDateFormat(
        "yyyy-MM-dd kk:mm:ss E",
        Locale("ko", "KR")
    ).format(Date(date))
}