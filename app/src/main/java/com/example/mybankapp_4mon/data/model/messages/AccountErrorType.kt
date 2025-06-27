package com.example.mybankapp_4mon.data.model.messages

enum class AccountErrorType(val message: String) {
    ACCOUNT_FETCH_ERROR("Ошибка загрузки счетов"),
    ACCOUNT_ADD_ERROR("Олибка добавления ssex"),
    NETWORK_ERROR ("Ошибка сети"),
    ACCOUNT_UPDATE_ERROR("Ошибка при обновлении счёта"),
    ACCOUNT_STATUS_PATCH_ERROR("Ошибка при изменении статуса счёта"),
    ACCOUNT_DELETE_ERROR("Ошибка при удалении счёта"),
}

fun AccountErrorType.errorMessage(msg: String): String{
    return "$this: $msg"
}