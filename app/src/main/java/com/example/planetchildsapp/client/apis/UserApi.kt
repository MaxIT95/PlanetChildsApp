package com.example.planetchildsapp.client.apis

import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.client.model.request.RefreshTokenRequest
import com.example.planetchildsapp.client.model.request.UserProfileRequest
import com.example.planetchildsapp.client.model.request.UserRegistrationRequest
import com.example.planetchildsapp.client.model.response.AuthUserResponse
import com.example.planetchildsapp.client.model.response.PagedUserResponse
import com.example.planetchildsapp.client.model.response.ProfileResponse
import com.example.planetchildsapp.client.model.response.RefreshTokenResponse
import com.example.planetchildsapp.client.model.response.UserProfileResponse
import com.example.planetchildsapp.client.model.response.UserRegistrationResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    /**
     * Register a new user
     * Creates a new user account with the provided information
     */
    @POST("/api/user/register")
    suspend fun registerUser(
        @Body request: UserRegistrationRequest
    ): Response<UserRegistrationResponse>

    /**
     * Refresh access token
     * Issues new access and refresh tokens
     */
    @POST("/api/user/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    /**
     * Authorization a user
     * User authentication to access the service
     */
    @POST("/api/user/login")
    suspend fun loginUser(
        @Body request: AuthUserRequest
    ): Response<AuthUserResponse>

    /**
     * Search users with pagination
     * Retrieves a paginated list of users with optional filtering by name
     */
    @GET("/api/user")
    suspend fun getAllUsers(
        @Query("name") name: String? = null,
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedUserResponse>

    /**
     * Get user and profile information
     * Retrieves detailed information about a specific user including their profile
     */
    @GET("/api/user/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Long
    ): Response<UserProfileResponse>

    /**
     * Update user and profile information
     */
    @PUT("/api/user/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Long,
        @Body request: UserProfileRequest
    ): Response<UserProfileResponse>

    /**
     * Delete user account
     * Permanently deletes a user account from both the application and Keycloak
     */
    @DELETE("/api/user/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Long
    ): Response<Unit>

    /**
     * Get current user and profile information
     * Retrieves information about the currently authenticated user including their profile
     */
    @GET("/api/user/me")
    suspend fun getCurrentUser(): Response<UserProfileResponse>

    /**
     * Upload or update profile photo
     * Uploads a new profile photo or replaces an existing one
     */
    @Multipart
    @PUT("/api/user/profile/{profileId}/photo")
    suspend fun setProfilePhoto(
        @Path("profileId") profileId: Long,
        @Part photo: MultipartBody.Part
    ): Response<ProfileResponse>

    /**
     * Delete profile photo
     * Removes the current profile photo
     */
    @DELETE("/api/user/profile/{profileId}/photo")
    suspend fun deleteProfilePhoto(
        @Path("profileId") profileId: Long
    ): Response<Unit>
}
