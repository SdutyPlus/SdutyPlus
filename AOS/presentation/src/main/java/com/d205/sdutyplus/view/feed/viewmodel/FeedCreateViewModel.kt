package com.d205.sdutyplus.view.feed.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.usecase.feed.CreateFeedUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FeedCreateViewModel"

@HiltViewModel
class FeedCreateViewModel @Inject constructor(
    private val createFeedUseCase: CreateFeedUseCase
): ViewModel() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    // FeedCreateFragment의 Image와 Content 내용을 가지고 있어야 하며,
    // 이를 FeedCreateUseCase에서 서버에 저장할 수 있도록 해주어야 한다.
    private val _bitmap = MutableLiveData<Bitmap?>()
    val bitmap : LiveData<Bitmap?>
        get() = _bitmap

    private val _image = MutableLiveData<Drawable?>(null)
    val image: LiveData<Drawable?>
        get() = _image

    private val _content = MutableLiveData<String?>("")
    val content: LiveData<String?>
        get() = _content
    fun setContent(content: String) {
        _content.value = content
    }

    private val _loadingFlag = MutableLiveData(false)
    val loadingFlag : LiveData<Boolean>
        get() = _loadingFlag


    private val _isFeedCreated = MutableLiveData<Boolean?>()
    val isFeedCreated: LiveData<Boolean?>
        get() = _isFeedCreated

    private val _isFeedCreateFailed = MutableLiveData<Boolean?>()
    val isFeedCreateFailed: LiveData<Boolean?>
        get() = _isFeedCreateFailed
    fun setIsFeedCreateFailed(isFeedCreateFailed: Boolean) {
        _isFeedCreateFailed.value = isFeedCreateFailed
    }

    // Deco에서 꾸민 Image를 여기서 Update해줘야 하며,
    // Image값에 따라 vm을 이용한, src 표기값이 달라진다.
    fun setBitmapAndImage(bitmap: Bitmap) {
        _bitmap.value = bitmap
        _image.value = BitmapDrawable(bitmap)
    }
    // Feed를 생성할 때, 값이 다 채워져 있는지 확인하고 UseCase를 실행시켜야 한다. (-> 맞나?)
        // -> 분기 후, UI 피드백이 바로 필요하다고 느껴서 Fragment에서 확인하고 이를 실행시키기로 함.
    // UseCase를 거쳐 최종적으로 FeedRemoteDataSource에서 api(POST('feed'))로 보내줘야 하며,
    fun createFeed() {
        viewModelScope.launch(defaultDispatcher) {
            createFeedUseCase(_bitmap.value!!, _content.value!!).collect() {
                if (it is ResultState.Success) {
                    Log.d(TAG, "createFeed: Success")
                    viewModelScope.launch(Dispatchers.Main) {
                        _isFeedCreated.value = true
                        _loadingFlag.value = false
                    }
                }
                else if (it is ResultState.Loading) {
                    Log.d(TAG, "createFeed: Loading")
                    _loadingFlag.postValue(true)
                }
                else {
                    Log.d(TAG, "createFeed: not Success!!")
                    _isFeedCreateFailed.postValue(true)
                    _loadingFlag.postValue(false)
                }
            }
        }
    }

    fun clearState() {
        _bitmap.value = null
        _image.value = null
        _content.value = ""
        _isFeedCreated.value = false
        _isFeedCreateFailed.value = false
    }
}