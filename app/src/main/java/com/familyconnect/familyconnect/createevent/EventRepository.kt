package com.familyconnect.familyconnect.createevent

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject


interface CreateEventRepository {
    suspend fun createEvent(event: CreateEventRequest): Response<EventResponse>
}


class CreateEventRepositoryImpl @Inject constructor(
    private val createEventApiService: CreateEventApiService
) : CreateEventRepository {
    override suspend fun createEvent(event: CreateEventRequest): Response<EventResponse> {
        return createEventApiService.createEvent(event)
    }
}



data class CreateEventRequest(
    val name: String,
    val description: String,
    val eventDate: String,
    val familyId: Int
)

data class EventResponse(
    val id: Int,
    val name: String,
    val description: String,
    val eventDate: String,
    val familyId: Int
)
