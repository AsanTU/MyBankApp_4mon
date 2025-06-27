package com.example.mybankapp_4mon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybankapp_4mon.data.api.ApiClient
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.data.model.messages.AccountErrorType
import com.example.mybankapp_4mon.data.model.AccountStatusPatch
import com.example.mybankapp_4mon.data.model.messages.AccountSuccessType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel : ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadAccounts() {
        ApiClient.accountApi.getAccounts().handlerResponse(
            onSuccess = { _accounts.value = it },
            onError = { _errorMessage.value = "Ошибка загрузки счетов: $it" }
        )
    }

    fun addAccount(account: Account) {
        ApiClient.accountApi.createAccount(account).handlerResponse(
            onSuccess = { loadAccounts() },
            onError = {
                _errorMessage.value = "Ошибка добавления счёта: $it"
            }
        )
    }

    fun updateAccount(account: Account) {
        ApiClient.accountApi.updateAccountFully(
            id = account.accountId!!,
            account = account
        ).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "Ошибка обновления счёта: $it"
            }
        )
    }

    fun patchAccountStatus(id: String, isActive: Boolean) {
        ApiClient.accountApi.patchAccountStatus(id, AccountStatusPatch(isActive)).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "Ошибка обновления статуса счёта: $it"
            }
        )
    }

    fun deleteAccount(id: String) {
        ApiClient.accountApi.deleteAccount(id).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "Ошибка удаления счёта: $it"
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