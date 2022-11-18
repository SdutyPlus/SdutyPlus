package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ListItemMystudyBinding
import com.d108.sduty.model.dto.Study

class MyStudyAdapter(var list: List<Study>) : RecyclerView.Adapter<MyStudyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemMystudyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Study){
            binding.studyList = item
        }
        fun bindClickListener(listener: OnStudyItemClick){
            binding.root.setOnClickListener {
                listener.onClick(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mylistStudyItemBinding =
            ListItemMystudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mylistStudyItemBinding).apply {
            bindClickListener(onStudyItemClick)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    interface OnStudyItemClick{
        fun onClick(view: View, position: Int)
    }
    lateinit var onStudyItemClick: OnStudyItemClick





}