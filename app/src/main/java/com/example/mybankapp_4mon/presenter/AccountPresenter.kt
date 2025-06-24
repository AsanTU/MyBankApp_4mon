package com.example.mybankapp_4mon.presenter

import com.example.mybankapp_4mon.data.model.Account

class AccountPresenter(val view: AccountContract.View): AccountContract.Presenter {
    override fun loadAccounts() {
        val testAccountList = listOf(
            Account(
                id = "1",
                name = "Test Account 1",
                balance = 1000,
                currency = "USD",
                isActive = true
            ),
            Account(
                id = "2",
                name = "Test Account 2",
                balance = 2000,
                currency = "EUR",
                isActive = false
            ),
            Account(
                id = "3",
                name = "Test Account 3",
                balance = 3000,
                currency = "RUB",
                isActive = true
            ),
        )
        view.showAccounts(testAccountList)
    }
}