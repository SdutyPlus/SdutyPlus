package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemQnaBinding
import com.d108.sduty.model.dto.Qna

class QnaAdapter: RecyclerView.Adapter<QnaAdapter.ViewHolder>() {
    var list = mutableListOf<Qna>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemQnaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.apply {
                data = list[absoluteAdapterPosition]
                root.setOnClickListener {
                    onClickQnaListener.onClick(list[absoluteAdapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemQnaBinding = ItemQnaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemQnaBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    lateinit var onClickQnaListener: OnClickQnaListener
    interface OnClickQnaListener{
        fun onClick(qna: Qna)
    }
}