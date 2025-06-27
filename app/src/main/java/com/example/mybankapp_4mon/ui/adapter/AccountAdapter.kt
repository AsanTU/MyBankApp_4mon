package com.example.mybankapp_4mon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mybankapp_4mon.data.model.Account
import com.example.mybankapp_4mon.databinding.ItemAccountBinding

class AccountAdapter(
    val onEdit: (Account) -> Unit,
    val onDelete: (String) -> Unit,
    val onStatusToggle: (String, Boolean) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

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
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) = with(binding) {
            account.apply {
                tvName.text = name
                tvBalance.text = balance.toString()
                btnEdit.setOnClickListener {
                    onEdit(this)
                }
                btnDelete.setOnClickListener {
                    accountId?.let { onDelete(it) }
                }
                switchActive.apply {
                    setOnCheckedChangeListener(null)
                    isChecked = isActive
                    setOnCheckedChangeListener { _, isChecked ->
                        accountId?.let { onStatusToggle(it, isChecked) }
                    }
                }
            }
        }
    }
}