package com.d108.sduty.ui.main.timer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ListItemTaskBinding
import com.d108.sduty.model.dto.Task

class TaskListAdapter(val fragmentActivity: FragmentActivity, var list: List<Task>, val colorPalette: List<Int>) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task, position: Int) {
            binding.task = item
            binding.taskColor = fragmentActivity.resources.getColor(colorPalette[position % 4])
        }

        fun bindClickListener(listener: OnClickTaskItem) {
            binding.root.setOnClickListener {
                listener.onClick(it, fragmentActivity, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemTaskBinding =
            ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemTaskBinding).apply {
            bindClickListener(onClickTaskItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = list.size


    interface OnClickTaskItem {
        fun onClick(view: View, fragmentActivity: FragmentActivity, position: Int)
    }

    lateinit var onClickTaskItem: OnClickTaskItem
}