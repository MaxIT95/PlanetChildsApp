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
    @POST("/api/coach-children/link-child")
    suspend fun linkChild(
        @Body request: LinkChildRequest
    ): Response<CoachChildrenResponse>

    /**
     * Get current coach's linked children
     * Retrieves all children linked to the currently authenticated coach with pagination
     */
    @GET("/api/coach-children")
    suspend fun getMyLinkedChildren(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedCoachChildrenResponse>

    /**
     * Unlink a child from coach
     * Removes the link between the coach and a child
     */
    @DELETE("/api/coach-children/{coachChildrenId}")
    suspend fun unlinkChild(
        @Path("coachChildrenId") coachChildrenId: Long
    ): Response<Unit>
}
