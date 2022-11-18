package com.d108.sduty.ui.main.home.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "HomeViewModel"
class HomeViewModel: ViewModel() {
    private val _image = MutableLiveData<Drawable?>(null)
    val image: LiveData<Drawable?>
        get() = _image

    private val _bitmap = MutableLiveData<Bitmap?>()
    val bitmap : LiveData<Bitmap?>
        get() = _bitmap
    fun setStoryImage(bitmap: Bitmap?){
        _bitmap.postValue(bitmap)
        _image.postValue(BitmapDrawable(bitmap))
    }
    fun clearStoryImage(){
        _image.value = null
    }
}

