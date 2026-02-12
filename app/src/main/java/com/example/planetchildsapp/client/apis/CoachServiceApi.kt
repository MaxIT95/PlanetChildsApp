package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.CoachServiceRequest
import com.example.planetchildsapp.client.model.response.CoachServiceResponse
import com.example.planetchildsapp.client.model.response.PagedCoachServiceResponse
import retrofit2.Response
import retrofit2.http.*

interface CoachServiceApi {

    @GET("/api/coach-service")
    suspend fun searchCoachServices(
        @Query("userId") userId: Long? = null,
        @Query("dictServiceId") dictServiceId: Long? = null,
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedCoachServiceResponse>

    @POST("/api/coach-service")
    suspend fun createCoachService(
        @Body request: CoachServiceRequest
    ): Response<CoachServiceResponse>

    @GET("/api/coach-service/{coachServiceId}")
    suspend fun getCoachServiceById(
        @Path("coachServiceId") coachServiceId: Long
    ): Response<CoachServiceResponse>

    @PUT("/api/coach-service/{coachServiceId}")
    suspend fun updateCoachService(
        @Path("coachServiceId") coachServiceId: Long,
        @Body request: CoachServiceRequest
    ): Response<CoachServiceResponse>

    @DELETE("/api/coach-service/{coachServiceId}")
    suspend fun deleteCoachService(
        @Path("coachServiceId") coachServiceId: Long
    ): Response<Unit>

    @GET("/api/coach-service/me")
    suspend fun getMyCoachServices(
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedCoachServiceResponse>
}
