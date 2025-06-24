package com.example.mybankapp_4mon.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.databinding.ActivityMainBinding
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
        presenter = AccountPresenter(view = this)
        presenter.loadAccounts()
    }

    private fun initAdapter() {
        accountAdapter = AccountAdapter()
        binding.rvAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvAccounts.adapter = accountAdapter
    }

    override fun showAccounts(accounts: List<Account>) {
        accountAdapter.setItems(accounts)

    }
}