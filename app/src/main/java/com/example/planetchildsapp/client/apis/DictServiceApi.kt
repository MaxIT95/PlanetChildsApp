package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.DictServiceRequest
import com.example.planetchildsapp.client.model.response.DictServiceResponse
import com.example.planetchildsapp.client.model.response.PagedDictServiceResponse
import retrofit2.Response
import retrofit2.http.*

interface DictServiceApi {

    /**
     * Search dictionary services with pagination
     * Retrieves a paginated list of dictionary services with optional filtering by name
     */
    @GET("/api/dict-service")
    suspend fun searchDictServices(
        @Query("name") name: String? = null,
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedDictServiceResponse>

    /**
     * Create a new dictionary service
     * Creates a new dictionary service entry
     */
    @POST("/api/dict-service")
    suspend fun createDictService(
        @Body request: DictServiceRequest
    ): Response<DictServiceResponse>

    /**
     * Get dictionary service by ID
     * Retrieves a specific dictionary service by its ID
     */
    @GET("/api/dict-service/{serviceId}")
    suspend fun getDictServiceById(
        @Path("serviceId") serviceId: Long
    ): Response<DictServiceResponse>

    /**
     * Update dictionary service
     * Updates an existing dictionary service
     */
    @PUT("/api/dict-service/{serviceId}")
    suspend fun updateDictService(
        @Path("serviceId") serviceId: Long,
        @Body request: DictServiceRequest
    ): Response<DictServiceResponse>

    /**
     * Delete dictionary service
     * Deletes a dictionary service by its ID
     */
    @DELETE("/api/dict-service/{serviceId}")
    suspend fun deleteDictService(
        @Path("serviceId") serviceId: Long
    ): Response<Unit>
}
