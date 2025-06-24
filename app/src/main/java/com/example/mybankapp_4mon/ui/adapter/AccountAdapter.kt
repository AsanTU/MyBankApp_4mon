package com.example.mybankapp_4mon.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybankapp_4mon.R
import com.example.mybankapp_4mon.data.model.Account

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private val items = mutableListOf<Account>()

    fun setItems(accounts: List<Account>) {
        items.clear()
        items.addAll(accounts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(account: Account) = with(itemView) {
            findViewById<TextView>(R.id.tv_name).text = account.name
            findViewById<TextView>(R.id.tv_balance).text = account.balance.toString()
        }
    }
}