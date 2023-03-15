package com.d205.sdutyplus.view.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.databinding.ListItemTaskBinding

class TaskAdapter(private val listener: TaskAdapterListener) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutTask.setOnClickListener {
                listener.onTaskClicked(getItem(adapterPosition))
            }
        }

        fun bind(taskDto: Task) {
            binding.apply {
                task = taskDto
                rvSubtask.apply {
                    adapter = SubTaskAdapter(taskDto.contents)
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.seq == newItem.seq
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}