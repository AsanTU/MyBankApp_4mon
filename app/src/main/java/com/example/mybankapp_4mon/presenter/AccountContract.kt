package com.example.mybankapp_4mon.presenter

import com.example.mybankapp_4mon.data.model.Account

interface AccountContract {
    interface View{
        fun showAccounts(accounts: List<Account>)
    }
    interface Presenter{
        fun loadAccounts()
    }
}