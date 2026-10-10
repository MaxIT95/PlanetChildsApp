package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.LinkChildRequest
import com.example.planetchildsapp.client.model.response.CoachChildrenResponse
import com.example.planetchildsapp.client.model.response.PagedCoachChildrenResponse
import retrofit2.Response
import retrofit2.http.*

interface CoachChildrenApi {

    /**
     * Link a child to the current coach
     * Creates a link between the authenticated coach and a child
     */
    @POST("/api/coach-clients/link-child")
    suspend fun linkChild(
        @Body request: LinkChildRequest
    ): Response<CoachChildrenResponse>

    /**
     * Get current coach's linked children
     * Retrieves all children linked to the currently authenticated coach with pagination
     */
    @GET("/api/coach-clients")
    suspend fun getMyLinkedChildren(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedCoachChildrenResponse>

    /**
     * Unlink a child from coach
     * Removes the link between the coach and a child
     */
    @DELETE("/api/coach-clients/{coachClientsId}")
    suspend fun unlinkChild(
        @Path("coachClientsId") coachClientsId: Long
    ): Response<Unit>
}
