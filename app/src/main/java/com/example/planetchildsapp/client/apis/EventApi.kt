package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.response.EventResponse
import com.example.planetchildsapp.client.model.response.PagedEventResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EventApi {

    /**
     * Get all events with pagination
     * Retrieves a paginated list of events
     */
    @GET("/api/event")
    suspend fun getAllEvents(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedEventResponse>

    /**
     * Create a new event
     * Creates a new event with description, date, and optional picture
     */
    @Multipart
    @POST("/api/event")
    suspend fun createEvent(
        @Part("description") description: RequestBody,
        @Part("eventDate") eventDate: RequestBody,
        @Part picture: MultipartBody.Part? = null
    ): Response<EventResponse>

    /**
     * Get event by ID
     * Retrieves a specific event by its ID
     */
    @GET("/api/event/{eventId}")
    suspend fun getEventById(
        @Path("eventId") eventId: Long
    ): Response<EventResponse>

    /**
     * Update event
     * Updates an existing event
     */
    @Multipart
    @PUT("/api/event/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: Long,
        @Part("description") description: RequestBody? = null,
        @Part("eventDate") eventDate: RequestBody? = null,
        @Part picture: MultipartBody.Part? = null
    ): Response<EventResponse>

    /**
     * Delete event
     * Deletes an event by its ID
     */
    @DELETE("/api/event/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: Long
    ): Response<Unit>
}
