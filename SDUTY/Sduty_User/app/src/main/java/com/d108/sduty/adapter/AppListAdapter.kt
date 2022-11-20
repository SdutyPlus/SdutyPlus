package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ListItemAppInfoBinding
import com.d108.sduty.model.AppInfo


class AppListAdapter(val fragmentActivity: FragmentActivity) :
    RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    var list = listOf<AppInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(private val binding: ListItemAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppInfo) {
            binding.appInfo = item
        }

        fun bindClickListener(listener: OnClickAppInfoItem) {
            binding.root.setOnClickListener {
                listener.onClick(it, fragmentActivity, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemAppInfoBinding =
            ListItemAppInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemAppInfoBinding).apply {
            bindClickListener(onClickAppInfoItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size


    interface OnClickAppInfoItem {
        fun onClick(view: View, fragmentActivity: FragmentActivity, position: Int)
    }

    lateinit var onClickAppInfoItem: OnClickAppInfoItem
}