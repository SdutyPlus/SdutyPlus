package com.d205.sdutyplus.uitls

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.d205.sdutyplus.R
import java.util.*

private const val TAG ="BindingAdapter"

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, src: String?){

    Glide.with(view.context)
        .load(src)
        .error(R.drawable.ic_empty_image)
        .into(view)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("percentage")
fun percentage(view: TextView, per: Int) {
    view.setText("$per%")
}

//@BindingAdapter("profileHashTagText")
//fun profileHashTagText(view: TextView, profile: Profile?){
//    var interest = ""
//    if(profile == null) return
//
//    if(!profile.interestHashtags.isNullOrEmpty()){
//        for(item in profile.interestHashtags!!){
//            interest = "$interest ${item.name}"
//        }
//    }
//    view.text = "${profile.job} /$interest"
//}
//
//@BindingAdapter("interestHashTagText")
//fun interestHashTagText(view: TextView, list: MutableList<InterestHashtag>?){
//    if(!list.isNullOrEmpty()){
//        var text = ""
//        for(item in list!!){
//            text = "${text} #${item.name} "
//        }
//        view.text = text
//    }else{
//        view.text = ""
//    }
//}
//
//@SuppressLint("ResourceAsColor")
//@BindingAdapter("userProfile", "myProfile")
//fun followButtonText(view: TextView, userProfile: Profile?, myProfile: Profile){
//    if(myProfile.follows?.get("${userProfile?.userSeq}") != null){
//        view.text = "팔로우 취소"
//        view.setBackgroundResource(R.drawable.button_gray)
//    }else{
//        view.text = "팔로우"
//        view.setBackgroundResource(R.drawable.button_app_blue)
//    }
//}
//
//@BindingAdapter("jobSeqToJobName")
//fun jobSeqToJobName(view: TextView, jobSeq: Int){
//    view.text = ApplicationClass.jobTagMap[jobSeq]
//}
//
//@BindingAdapter("dateToYYYMMDD")
//fun dateToYYYMMDD(view: TextView, date: Date?){
//    if (date == null){
//        view.text = ""
//    }else{
//        view.text = convertTimeDateToString(date!!, "yyyyMMdd")
//    }
//
//}
//
//@BindingAdapter("emailStart")
//fun emailStart(view: TextView, email: String?){
//    if(email == null){
//        view.text = ""
//    }else {
//        view.text = email.substring(0, email.indexOf("@"))
//    }
//}
//@BindingAdapter("emailEnd")
//fun emailEnd(view: TextView, email: String?){
//    if(email == null){
//        view.text = ""
//    }else {
//        view.text = email.substring(email.indexOf("@") + 1, email.length)
//    }
//}