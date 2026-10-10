package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.ScheduleRowCreateRequest
import com.example.planetchildsapp.client.model.request.ScheduleRowStatusUpdateRequest
import com.example.planetchildsapp.client.model.response.PagedScheduleRowResponse
import com.example.planetchildsapp.client.model.response.ScheduleRowResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleRowApi {

    /**
     * Request inclusion in a trainer's schedule.
     * Client submits a request with PENDING status; trainer must approve or decline.
     */
    @POST("/api/schedule-row")
    suspend fun createScheduleRow(
        @Body request: ScheduleRowCreateRequest
    ): Response<ScheduleRowResponse>

    /**
     * Get schedule row requests for a schedule.
     * Trainer retrieves all client requests for a specific schedule, optionally filtered by status.
     */
    @GET("/api/schedule-row")
    suspend fun getScheduleRows(
        @Query("scheduleId") scheduleId: Long,
        @Query("status") status: String? = null,
        @Query("pageNumber") pageNumber: Int = 0,
        @Query("pageSize") pageSize: Int = 20
    ): Response<PagedScheduleRowResponse>

    /**
     * Approve or decline a schedule row request.
     * Trainer updates the status of a pending client request.
     */
    @PATCH("/api/schedule-row/{scheduleRowId}")
    suspend fun updateScheduleRowStatus(
        @Path("scheduleRowId") scheduleRowId: Long,
        @Body request: ScheduleRowStatusUpdateRequest
    ): Response<ScheduleRowResponse>
}
