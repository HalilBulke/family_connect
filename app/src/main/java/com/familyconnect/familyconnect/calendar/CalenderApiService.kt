package com.familyconnect.familyconnect.calendar

import com.familyconnect.familyconnect.task.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CalenderApiService {
    @GET("calendar/getCalendar/{username}")
    suspend fun getCalendar(@Path("username") username: String): Response<List<CalenderResponse>>
}
