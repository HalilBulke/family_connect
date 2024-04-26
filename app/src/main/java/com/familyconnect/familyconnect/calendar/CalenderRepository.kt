package com.familyconnect.familyconnect.calendar

import com.familyconnect.familyconnect.task.Task
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject

interface CalenderRepository {
    suspend fun getCalendar(userName: String): Response<List<CalenderResponse>>
}

class NetworkCalenderRepository @Inject constructor(
    private val calenderApiService: CalenderApiService
) : CalenderRepository {
    override suspend fun getCalendar(userName: String): Response<List<CalenderResponse>> {
        return calenderApiService.getCalendar(userName)
    }
}

data class CalenderResponse(
    @SerializedName("id") val id:Int?,
    @SerializedName("priority") val priority:Int?,
    @SerializedName("status") val status:String?,
    @SerializedName("name") val name:String?,
    @SerializedName("description") val description:String?,
    @SerializedName("type") val type:String?,
    @SerializedName("role") val role:String?,
    @SerializedName("dueDate") val dueDate:String?,
    @SerializedName("startDate") val startDate:String?,
)
