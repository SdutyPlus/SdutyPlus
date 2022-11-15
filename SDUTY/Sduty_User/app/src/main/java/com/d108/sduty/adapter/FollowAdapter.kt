package com.d108.sduty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.common.FLAG_FOLLOWER
import com.d108.sduty.databinding.ItemFollowBinding
import com.d108.sduty.model.dto.Follow
import com.d108.sduty.model.dto.Profile

private const val TAG ="FollowAdapter"
class FollowAdapter(val mySeq: Int, var myProfile: Profile): RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    var list = mutableListOf<Follow>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var tabFlag = 0
    inner class ViewHolder(val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(follow: Follow){
            binding.apply {
                data = follow
                var flagMe = false
                if(mySeq != follow.profile!!.userSeq){
                    if(tabFlag == FLAG_FOLLOWER) {
                        if(mySeq == follow.followerSeq)
//                            btnFollow.text = "취소"
                        else{


                            btnFollow.text = "프로필 보기"
//                        btnFollow.visibility = View.VISIBLE
                        }
                    }
                    else{
//                        btnFollow.visibility = View.GONE
                    }

                }else{
                    flagMe = true
                    btnFollow.text = "본인"
                }
//                if(myProfile.follows!!.get("${follow.followeeSeq}") != null){
//                    btnFollow.text = "취소"
//                }

               constProfile.setOnClickListener {
                    onClickFollowListener.onClickProfile(follow)
                }
                if(!flagMe) {
                    btnFollow.setOnClickListener {
                        onClickFollowListener.onClickFollowBtn(follow)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemFollowBinding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemFollowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    lateinit var onClickFollowListener: OnClickFollowListener
    interface OnClickFollowListener{
        fun onClickFollowBtn(follow: Follow)
        fun onClickProfile(follow: Follow)
    }
}