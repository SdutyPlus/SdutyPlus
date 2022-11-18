package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ListItemCamstudyMemberBinding
import com.d108.sduty.model.dto.Member


class CamStudyMemberAdapter(var list: List<Member>): RecyclerView.Adapter<CamStudyMemberAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemCamstudyMemberBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Member){
            binding.studyMember = item
            binding.constCamstudyMember.setOnClickListener {
                onClickMemberListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val memberlistStudyItemBinding =
            ListItemCamstudyMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(memberlistStudyItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    lateinit var onClickMemberListener: OnClickMemberListener
    interface OnClickMemberListener{
        fun onClick(member: Member)
    }

}