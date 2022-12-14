package co.kr.kwon.deliveryapp.data.repository.order

sealed class Result {

    data class Success<T>(
        val data: T? = null
    ) : Result()

    data class Error(
        val e: Throwable
    ) : Result()
}
