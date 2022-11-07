package com.d205.sdutyplus.view.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.SubTask
import com.d205.sdutyplus.databinding.ListItemSubtaskBinding

class SubTaskAdapter(private val listener: SubTaskAdapterListener)
    :ListAdapter<SubTask, SubTaskAdapter.ViewHolder>(diffUtil) {

        inner class ViewHolder(private val binding: ListItemSubtaskBinding): RecyclerView.ViewHolder(binding.root){
            init {
                binding.root.setOnClickListener {
                    listener.onSubTaskClicked(getItem(adapterPosition))
                }
            }
            fun bind(subTask: SubTask){
                binding.subTask = subTask
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemSubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<SubTask>(){
            override fun areItemsTheSame(oldItem: SubTask, newItem: SubTask): Boolean {
                return oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: SubTask, newItem: SubTask): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }
}