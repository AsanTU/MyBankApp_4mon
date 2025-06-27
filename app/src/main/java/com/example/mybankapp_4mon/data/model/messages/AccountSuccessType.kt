package com.example.mybankapp_4mon.data.model.messages

enum class AccountSuccessType(val message: String) {
    ACCOUNT_STATUS_SUCCESS("Состояние статуса счета обновлен"),
    ACCOUNT_UPDATE_SUCCESS("Счёт успешно обновлён"),
    ACCOUNT_DELETE_SUCCESS("Счёт успешно удалён")
}