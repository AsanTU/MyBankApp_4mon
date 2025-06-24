package com.example.mybankapp_4mon.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.databinding.ActivityMainBinding
import com.example.mybankapp_4mon.databinding.DialogAddAccountBinding
import com.example.mybankapp_4mon.presenter.AccountContract
import com.example.mybankapp_4mon.presenter.AccountPresenter
import com.example.mybankapp_4mon.ui.adapter.AccountAdapter

class MainActivity : AppCompatActivity(), AccountContract.View {

    private lateinit var presenter: AccountContract.Presenter
    private lateinit var accountAdapter: AccountAdapter

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initClicks()
        presenter = AccountPresenter(view = this)
        presenter.loadAccounts()
    }

    private fun initAdapter() {
        accountAdapter = AccountAdapter()
        binding.rvAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvAccounts.adapter = accountAdapter
    }

    private fun initClicks(){
        with(binding){
            btnAdd.setOnClickListener {
                showAddDialog { presenter.addAccount(it) }
            }
        }
    }

    private fun showAddDialog(action: (Account) -> Unit){
        val dialogVB = DialogAddAccountBinding.inflate(LayoutInflater.from(this))

        AlertDialog.Builder(this)
            .setTitle("Добавить счет")
            .setView(dialogVB.root)
            .setPositiveButton("Добавить") { dialog, _ ->
                with(dialogVB) {
                    val name = etName.text.toString()
                    val balance = etBalance.text.toString().toInt()
                    val currency = etCurrency.text.toString()
                    val account = Account(
                        name = name,
                        balance = balance,
                        currency = currency,
                        isActive = true
                    )
                    action.invoke(account)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun showAccounts(accounts: List<Account>) {
        accountAdapter.setItems(accounts)

    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}