package com.d205.sdutyplus.view.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.databinding.ListItemTaskBinding

class ReportAdapter(private val listener: ReportAdapterListener)
    : ListAdapter<Task, ReportAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ListItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                listener.onItemClicked(getItem(adapterPosition))
            }
        }
        fun bind(task: Task) {
            binding.task = task
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }
}