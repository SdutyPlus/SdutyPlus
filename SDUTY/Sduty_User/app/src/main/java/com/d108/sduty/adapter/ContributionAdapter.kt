package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemContributionBinding
import com.d108.sduty.ui.main.mypage.viewmodel.ContributionViewModel

class ContributionAdapter: RecyclerView.Adapter<ContributionAdapter.ViewHolder>() {
    var list = listOf<Boolean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(val binding: ItemContributionBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.vm = ContributionViewModel()
        }
        fun bind(boolean: Boolean){
            binding.vm!!.setContributionItem(boolean)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemContributionBinding =
            ItemContributionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemContributionBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}