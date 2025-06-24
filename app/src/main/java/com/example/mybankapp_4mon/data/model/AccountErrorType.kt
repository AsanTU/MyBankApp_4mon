package com.example.mybankapp_4mon.data.model

enum class AccountErrorType(val message: String) {
    ACCOUNT_FETCH_ERROR("Ошибка загрузки счетов"),
    ACCOUNT_ADD_ERROR("Олибка добавления ssex"),
    NETWORK_ERROR ("Ошибка сети"),
}

fun AccountErrorType.errorMessage(msg: String): String{
    return "$this: $msg"
}