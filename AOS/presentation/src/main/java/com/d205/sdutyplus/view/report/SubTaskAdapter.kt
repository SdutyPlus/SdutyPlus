package com.d205.sdutyplus.view.report

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.SubTask
import com.d205.sdutyplus.databinding.ListItemSubtaskBinding

class SubTaskAdapter(private val subtask: List<SubTask>)
    :RecyclerView.Adapter<SubTaskAdapter.ViewHolder>() {

        inner class ViewHolder(private val binding: ListItemSubtaskBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(pos: Int){
                Log.d(TAG, "bind@@SubTask: ${subtask[pos]}")
                binding.subTask = subtask[pos]
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemSubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return subtask.size
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