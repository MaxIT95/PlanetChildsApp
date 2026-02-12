package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.ChildRequest
import com.example.planetchildsapp.client.model.response.ChildResponse
import com.example.planetchildsapp.client.model.response.PagedChildResponse
import retrofit2.Response
import retrofit2.http.*

interface ChildApi {

    /**
     * Add a new child
     * Creates a new child for the currently authenticated parent
     */
    @POST("/api/child/add-child")
    suspend fun addChild(
        @Body request: ChildRequest
    ): Response<ChildResponse>

    /**
     * Get current user's children
     * Retrieves all children for the currently authenticated parent with pagination
     */
    @GET("/api/child")
    suspend fun getMyChildren(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedChildResponse>

    /**
     * Get child by ID
     * Retrieves a specific child by ID
     */
    @GET("/api/child/{childId}")
    suspend fun getChildById(
        @Path("childId") childId: Long
    ): Response<ChildResponse>

    /**
     * Update child
     * Updates an existing child
     */
    @PUT("/api/child/{childId}")
    suspend fun updateChild(
        @Path("childId") childId: Long,
        @Body request: ChildRequest
    ): Response<ChildResponse>

    /**
     * Delete child
     * Deletes a child by ID
     */
    @DELETE("/api/child/{childId}")
    suspend fun deleteChild(
        @Path("childId") childId: Long
    ): Response<Unit>
}
