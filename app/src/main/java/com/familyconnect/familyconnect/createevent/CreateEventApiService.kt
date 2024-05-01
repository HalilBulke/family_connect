package com.familyconnect.familyconnect.createevent


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface CreateEventApiService {
    @POST("events/create")
    suspend fun createEvent(@Body event: CreateEventRequest): Response<EventResponse>
}
