package com.familyconnect.familyconnect.calendar

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CalendarApiService {
    @GET("calendar/getCalendar/{username}")
    suspend fun getCalendar(@Path("username") username: String): Response<List<CalenderResponse>>
}
