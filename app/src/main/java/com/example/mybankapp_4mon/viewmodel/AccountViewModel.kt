package com.example.mybankapp_4mon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybankapp_4mon.data.api.AccountApi
import com.example.mybankapp_4mon.di.NetworkModule
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.data.model.messages.AccountErrorType
import com.example.mybankapp_4mon.data.model.AccountStatusPatch
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountApi: AccountApi
) : ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadAccounts() {
        accountApi.getAccounts().handlerResponse(
            onSuccess = { _accounts.value = it },
            onError = { _errorMessage.value = "${AccountErrorType.ACCOUNT_FETCH_ERROR} $it" }
        )
    }

    fun addAccount(account: Account) {
        accountApi.createAccount(account).handlerResponse(
            onSuccess = { loadAccounts() },
            onError = {
                _errorMessage.value = "${AccountErrorType.ACCOUNT_ADD_ERROR}: $it"
            }
        )
    }

    fun updateAccount(account: Account) {
        accountApi.updateAccountFully(
            id = account.accountId!!,
            account = account
        ).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "${AccountErrorType.ACCOUNT_UPDATE_ERROR} $it"
            }
        )
    }

    fun patchAccountStatus(id: String, isActive: Boolean) {
        accountApi.patchAccountStatus(id, AccountStatusPatch(isActive)).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "${AccountErrorType.ACCOUNT_STATUS_PATCH_ERROR} $it"
            }
        )
    }

    fun deleteAccount(id: String) {
        accountApi.deleteAccount(id).handlerResponse(
            onSuccess = {
                loadAccounts()
            },
            onError = {
                _errorMessage.value = "${AccountErrorType.ACCOUNT_DELETE_ERROR} $it"
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