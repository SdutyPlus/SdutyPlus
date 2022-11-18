package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ListItemStudyBinding
import com.d108.sduty.model.dto.Study

class StudyListAdapter(var list: List<Study>): RecyclerView.Adapter<StudyListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemStudyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Study){
            binding.studyList = item
        }
        fun bindClickListener(listener: OnStudyItemClick){
            binding.root.setOnClickListener{
                listener.onClick(it, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listStudyItemBinding =
            ListItemStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listStudyItemBinding).apply {
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