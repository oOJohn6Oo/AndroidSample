package io.john6.sample.cleanarch.domain.data

import androidx.annotation.StringRes


data class DataState<T>(
    val data: T? = null,
    val errorResId: Int = 0,
    val errorMsg: String = "",
    val isLoading: Boolean = false,
){
    companion object{

        fun <T> success(data: T): DataState<T> = DataState(data = data)
        fun <T> error(message: String): DataState<T> = DataState(errorMsg = message)
        fun <T> error(@StringRes message: Int): DataState<T> = DataState(errorResId = message)
        fun <T> loading(): DataState<T> = DataState(isLoading = true)


    }

    fun isSuccess() = errorResId == 0 && errorMsg.isEmpty() && data != null
}