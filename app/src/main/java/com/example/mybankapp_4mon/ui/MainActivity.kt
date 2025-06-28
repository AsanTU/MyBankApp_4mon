package com.example.mybankapp_4mon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.databinding.ActivityMainBinding
import com.example.mybankapp_4mon.databinding.DialogAddAccountBinding
import com.example.mybankapp_4mon.viewmodel.AccountViewModel
import com.example.mybankapp_4mon.ui.adapter.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var accountAdapter: AccountAdapter

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initClicks()
        viewModel.loadAccounts()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.accounts.observe(this) {
            accountAdapter.setItems(it)
        }

        viewModel.successMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.errorMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initAdapter() {
        accountAdapter = AccountAdapter(
            onEdit = {
                showAccountDialog(it) { editedAccount -> viewModel.updateAccount(editedAccount) }
            },
            onStatusToggle = { id, isChecked ->
                viewModel.patchAccountStatus(id, isChecked)
            },
            onDelete = {
                viewModel.deleteAccount(it)
            }
        )
        binding.rvAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvAccounts.adapter = accountAdapter
    }

    private fun initClicks() {
        with(binding) {
            btnAdd.setOnClickListener {
                showAccountDialog { viewModel.addAccount(it) }
            }
        }
    }

    private fun showAccountDialog(account: Account? = null, action: (Account) -> Unit) {
        val dialogVB = DialogAddAccountBinding.inflate(LayoutInflater.from(this))

        with(dialogVB) {

            account?.let {
                etName.setText(it.name)
                etBalance.setText(it.balance.toString())
                etCurrency.setText(it.currency)
            }

            val isEditing = account != null
            val dialogTitle = if (isEditing) "Изменить счёт" else "Добавить счёт"
            val positiveButtonText = if (isEditing) "Сохранить" else "Добавить"

            AlertDialog.Builder(this@MainActivity)
                .setTitle(dialogTitle)
                .setView(dialogVB.root)
                .setPositiveButton(positiveButtonText) { _, _ ->
                    val name = etName.text.toString()
                    val balance = etBalance.text.toString().toIntOrNull() ?: 0
                    val currency = etCurrency.text.toString()

                    val newAccount = account?.copy(
                        name = name,
                        balance = balance,
                        currency = currency,
                        isActive = account.isActive
                    ) ?: Account(
                        name = name,
                        balance = balance,
                        currency = currency,
                        isActive = true
                    )
                    action.invoke(newAccount)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }
}