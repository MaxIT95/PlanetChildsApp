package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.ScheduleCreateRequest
import com.example.planetchildsapp.client.model.response.PagedScheduleResponse
import com.example.planetchildsapp.client.model.response.ScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {

    /**
     * Create a schedule for a trainer
     * Creates a new schedule with date, time range, and trainer information
     */
    @POST("/api/schedule/create")
    suspend fun createSchedule(
        @Body request: ScheduleCreateRequest
    ): Response<ScheduleResponse>

    /**
     * Get all schedules with pagination
     * Retrieves a paginated list of all available schedules.
     * Clients can see trainer name, date and time window.
     */
    @GET("/api/schedule")
    suspend fun getSchedules(
        @Query("pageNumber") pageNumber: Int = 0,
        @Query("trainerId") trainerId: Long? = null,
        @Query("pageSize") pageSize: Int = 20
    ): Response<PagedScheduleResponse>

    /**
     * Get schedule by ID
     * Retrieves a specific schedule by its ID
     */
    @GET("/api/schedule/{scheduleId}")
    suspend fun getScheduleById(
        @Path("scheduleId") scheduleId: Long
    ): Response<ScheduleResponse>
}
