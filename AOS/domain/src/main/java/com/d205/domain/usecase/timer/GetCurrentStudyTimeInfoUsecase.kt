package com.d205.domain.usecase.timer

import android.util.Log
import com.d205.domain.common.convertTimeDateToString
import com.d205.domain.common.convertTimeStringToDate
import com.d205.domain.model.timer.CurrentTaskDto
import java.util.Calendar
import javax.inject.Inject

class GetCurrentStudyTimeInfoUsecase @Inject constructor(
    private val getStartTimeUsecase: GetStartTimeUsecase,
    private val getElapsedTimeUsecase: GetElapsedTimeUsecase
){
    operator fun invoke(etime: Int): CurrentTaskDto { // todo dto return 시키기
        val startTime = getStartTimeUsecase()
        val elapsedTime = getElapsedTimeUsecase()
        val endTime = canculateEndTime(startTime, etime)

        return CurrentTaskDto(0, startTime, endTime)
    }

    private fun canculateEndTime(startTime: String, elapsedTime: Int): String {
        // 스타트 타임 00:00:00 을 Date 로 변환한다.
        val dateStartTime = convertTimeStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")

        // Date에 경과 시간을 더해 Date 종료 시간을 생성한다.
        // calendar 객체에 계산하려는 시작 날짜 설정
        val calendar = Calendar.getInstance()
        calendar.time = dateStartTime
        // 경과시간 (s) 더하고 Date EndTime 가져오기
        calendar.add(Calendar.SECOND, elapsedTime)
        val dateEndTime = calendar.time

        // String 종료 시간을 반환한다.
        val stringEndtime = convertTimeDateToString(dateEndTime, "yyyy-MM-dd HH:mm:ss")

        Log.d("EndTime","s $startTime e $elapsedTime e $stringEndtime")
        return stringEndtime
    }
}