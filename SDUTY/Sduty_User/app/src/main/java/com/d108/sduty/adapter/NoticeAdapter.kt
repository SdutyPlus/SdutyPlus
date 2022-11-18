package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemListNoticeBinding
import com.d108.sduty.model.dto.Notice

class NoticeAdapter: RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    var list =  listOf<Notice>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(private val binding: ItemListNoticeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Notice){
            binding.apply {
                noticeList = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listNoticeItemBinding =
            ItemListNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listNoticeItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

}