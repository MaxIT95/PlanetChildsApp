package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.CreateLinkRequestRequest
import com.example.planetchildsapp.client.model.response.CoachLinkRequestResponse
import com.example.planetchildsapp.client.model.response.PagedCoachLinkRequestResponse
import retrofit2.Response
import retrofit2.http.*

interface CoachLinkRequestApi {

    /**
     * Create a link request
     * Creates a link request from a client (or parent) to a coach
     */
    @POST("/api/coach-link-request")
    suspend fun createLinkRequest(
        @Body request: CreateLinkRequestRequest
    ): Response<CoachLinkRequestResponse>

    /**
     * Get current user's link requests
     * Retrieves link requests for the currently authenticated user with pagination
     */
    @GET("/api/coach-link-request")
    suspend fun getMyLinkRequests(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedCoachLinkRequestResponse>

    /**
     * Cancel or reject a link request
     * Cancels (by client) or rejects (by coach) a link request
     */
    @DELETE("/api/coach-link-request/{linkRequestId}")
    suspend fun deleteLinkRequest(
        @Path("linkRequestId") linkRequestId: Long
    ): Response<Unit>
}
