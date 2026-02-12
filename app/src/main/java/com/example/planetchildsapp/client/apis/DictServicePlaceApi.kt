package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.DictServicePlaceRequest
import com.example.planetchildsapp.client.model.response.DictServicePlaceResponse
import com.example.planetchildsapp.client.model.response.PagedDictServicePlaceResponse
import retrofit2.Response
import retrofit2.http.*

interface DictServicePlaceApi {

    @GET("/api/dict-service-place")
    suspend fun searchDictServicePlaces(
        @Query("name") name: String? = null,
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedDictServicePlaceResponse>

    @POST("/api/dict-service-place")
    suspend fun createDictServicePlace(
        @Body request: DictServicePlaceRequest
    ): Response<DictServicePlaceResponse>

    @GET("/api/dict-service-place/{placeId}")
    suspend fun getDictServicePlaceById(
        @Path("placeId") placeId: Long
    ): Response<DictServicePlaceResponse>

    @PUT("/api/dict-service-place/{placeId}")
    suspend fun updateDictServicePlace(
        @Path("placeId") placeId: Long,
        @Body request: DictServicePlaceRequest
    ): Response<DictServicePlaceResponse>

    @DELETE("/api/dict-service-place/{placeId}")
    suspend fun deleteDictServicePlace(
        @Path("placeId") placeId: Long
    ): Response<Unit>
}
