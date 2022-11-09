package com.d205.sdutyplus.view.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.SubTask
import com.d205.sdutyplus.databinding.ListItemSubtaskBinding

class SubTaskAdapter(private val subtask: List<SubTask>) :
    RecyclerView.Adapter<SubTaskAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemSubtaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.subTask = subtask[pos]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemSubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return subtask.size
    }
}