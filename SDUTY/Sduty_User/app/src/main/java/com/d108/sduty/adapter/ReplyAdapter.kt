package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemReplyBinding
import com.d108.sduty.model.dto.Reply

class ReplyAdapter(val userSeq: Int): RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {
    var list = mutableListOf<Reply>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(val binding: ItemReplyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                data = list[absoluteAdapterPosition]
                if(userSeq == list[absoluteAdapterPosition].userSeq){
                    btnOption.visibility = View.VISIBLE
                    btnOption.setOnClickListener {
                        onClickReplyListener.onClick(it, position)
                    }
                }
                ivProfile.setOnClickListener {
                    onClickReplyListener.onClickProfile(list[absoluteAdapterPosition].userSeq)
                }
                tvNickname.setOnClickListener {
                    onClickReplyListener.onClickProfile(list[absoluteAdapterPosition].userSeq)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemReplyBinding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemReplyBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size

    lateinit var onClickReplyListener: OnClickReplyListener
    interface OnClickReplyListener{
        fun onClick(view: View, position: Int)
        fun onClickProfile(userSeq: Int)
    }
}