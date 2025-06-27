package com.example.mybankapp_4mon.presenter

import android.util.Log
import com.example.mybankapp_4mon.data.api.ApiClient
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.data.model.messages.AccountErrorType
import com.example.mybankapp_4mon.data.model.AccountStatusPatch
import com.example.mybankapp_4mon.data.model.messages.AccountSuccessType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPresenter(val view: AccountContract.View) : AccountContract.Presenter {
    override fun loadAccounts() {
        ApiClient.accountApi.getAccounts().handlerResponse(
            onSuccess = { view.showAccounts(it) },
            onError = { view.showError("${AccountErrorType.ACCOUNT_FETCH_ERROR.message}: $it") }
        )
    }

    override fun addAccount(account: Account) {
        ApiClient.accountApi.createAccount(account).handlerResponse(
            onSuccess = { loadAccounts() },
            onError = {
                view.showError("${AccountErrorType.ACCOUNT_ADD_ERROR.message}: $it")
            }
        )
    }

    override fun updateAccount(account: Account) {
        ApiClient.accountApi.updateAccountFully(
            id = account.accountId!!,
            account = account
        ).handlerResponse(
            onSuccess = {
                view.showSuccess(AccountSuccessType.ACCOUNT_UPDATE_SUCCESS.message)
                loadAccounts()
            },
            onError = {
                view.showError("${AccountErrorType.ACCOUNT_UPDATE_ERROR.message}: $it}")
            }
        )
    }

    override fun patchAccountStatus(id: String, isActive: Boolean) {
        ApiClient.accountApi.patchAccountStatus(id, AccountStatusPatch(isActive)).handlerResponse(
            onSuccess = {
                view.showSuccess(AccountSuccessType.ACCOUNT_STATUS_SUCCESS.message)
                loadAccounts()
            },
            onError = {
                view.showError("${AccountErrorType.ACCOUNT_STATUS_PATCH_ERROR.message}: $it}")
            }
        )
    }

    override fun deleteAccount(id: String) {
        ApiClient.accountApi.deleteAccount(id).handlerResponse(
            onSuccess = {
                view.showSuccess(AccountSuccessType.ACCOUNT_DELETE_SUCCESS.message)
                loadAccounts()
            },
            onError = {
                view.showError("${AccountErrorType.ACCOUNT_DELETE_ERROR.message}: $it")
            }
        )
    }

    fun <T> Call<T>.handlerResponse(
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                if (response.isSuccessful && response.body() != null) onSuccess(response.body()!!)
                else onError(response.code().toString())
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                onError("${AccountErrorType.NETWORK_ERROR}: ${t.message}")
            }

        })
    }

}