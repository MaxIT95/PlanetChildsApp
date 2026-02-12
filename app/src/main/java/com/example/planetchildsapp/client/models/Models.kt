//package com.example.planetchildsapp.client.models
//
//import com.google.gson.annotations.SerializedName
//import java.time.LocalDate
//import java.time.LocalDateTime
//
//// Enums
//enum class Role {
//    @SerializedName("PARENT")
//    PARENT,
//    @SerializedName("COACH")
//    COACH,
//    @SerializedName("ADMIN")
//    ADMIN
//}
//
//// Auth Models
//data class AuthUserRequest(
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("password")
//    val password: String
//)
//
//data class AuthUserResponse(
//    @SerializedName("accessToken")
//    val accessToken: String,
//    @SerializedName("refreshToken")
//    val refreshToken: String,
//    @SerializedName("email")
//    val email: String
//)
//
//data class RefreshTokenRequest(
//    @SerializedName("refreshToken")
//    val refreshToken: String
//)
//
//data class RefreshTokenResponse(
//    @SerializedName("accessToken")
//    val accessToken: String,
//    @SerializedName("refreshToken")
//    val refreshToken: String
//)
//
//// User Models
//data class UserRegistrationRequest(
//    @SerializedName("role")
//    val role: Role,
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("password")
//    val password: String,
//    @SerializedName("firstName")
//    val firstName: String,
//    @SerializedName("lastName")
//    val lastName: String,
//    @SerializedName("middleName")
//    val middleName: String? = null
//)
//
//data class UserRegistrationResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("createdAt")
//    val createdAt: String
//)
//
//data class UserProfileRequest(
//    @SerializedName("email")
//    val email: String? = null,
//    @SerializedName("name")
//    val name: String? = null,
//    @SerializedName("description")
//    val description: String? = null,
//    @SerializedName("contents")
//    val contents: List<ProfileContentRequest>? = null
//)
//
//data class ProfileContentRequest(
//    @SerializedName("id")
//    val id: Long? = null,
//    @SerializedName("data")
//    val data: String? = null
//)
//
//data class UserProfileResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("createdAt")
//    val createdAt: String,
//    @SerializedName("updatedAt")
//    val updatedAt: String,
//    @SerializedName("profile")
//    val profile: ProfileResponse
//)
//
//data class ProfileResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("description")
//    val description: String? = null,
//    @SerializedName("contents")
//    val contents: List<ProfileContentResponse>? = null
//)
//
//data class ProfileContentResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("data")
//    val data: String
//)
//
//data class SearchUserResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("name")
//    val name: String
//)
//
//data class PagedUserResponse(
//    @SerializedName("content")
//    val content: List<SearchUserResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Child Models
//data class ChildRequest(
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("birthday")
//    val birthday: String
//)
//
//data class ChildResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("birthday")
//    val birthday: String,
//    @SerializedName("parent")
//    val parent: SearchUserResponse,
//    @SerializedName("createdAt")
//    val createdAt: String,
//    @SerializedName("updatedAt")
//    val updatedAt: String
//)
//
//data class PagedChildResponse(
//    @SerializedName("content")
//    val content: List<ChildResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Coach Children Models
//data class LinkChildRequest(
//    @SerializedName("childId")
//    val childId: Long
//)
//
//data class CoachChildrenResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("coach")
//    val coach: SearchUserResponse,
//    @SerializedName("child")
//    val child: ChildResponse,
//    @SerializedName("createdAt")
//    val createdAt: String
//)
//
//data class PagedCoachChildrenResponse(
//    @SerializedName("content")
//    val content: List<CoachChildrenResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Dictionary Service Models
//data class DictServiceRequest(
//    @SerializedName("name")
//    val name: String
//)
//
//data class DictServiceResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("createdAt")
//    val createdAt: String,
//    @SerializedName("updatedAt")
//    val updatedAt: String
//)
//
//data class PagedDictServiceResponse(
//    @SerializedName("content")
//    val content: List<DictServiceResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Dictionary Service Place Models
//data class DictServicePlaceRequest(
//    @SerializedName("name")
//    val name: String
//)
//
//data class DictServicePlaceResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("createdAt")
//    val createdAt: String,
//    @SerializedName("updatedAt")
//    val updatedAt: String
//)
//
//data class PagedDictServicePlaceResponse(
//    @SerializedName("content")
//    val content: List<DictServicePlaceResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Coach Service Models
//data class CoachServiceRequest(
//    @SerializedName("userId")
//    val userId: Long,
//    @SerializedName("dictServiceId")
//    val dictServiceId: Long,
//    @SerializedName("costOneTime")
//    val costOneTime: Double,
//    @SerializedName("costMembership")
//    val costMembership: Double,
//    @SerializedName("salary")
//    val salary: Double
//)
//
//data class CoachServiceResponse(
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("user")
//    val user: SearchUserResponse,
//    @SerializedName("dictService")
//    val dictService: DictServiceResponse,
//    @SerializedName("costOneTime")
//    val costOneTime: Double,
//    @SerializedName("costMembership")
//    val costMembership: Double,
//    @SerializedName("salary")
//    val salary: Double,
//    @SerializedName("createdAt")
//    val createdAt: String,
//    @SerializedName("updatedAt")
//    val updatedAt: String
//)
//
//data class PagedCoachServiceResponse(
//    @SerializedName("content")
//    val content: List<CoachServiceResponse>,
//    @SerializedName("page")
//    val page: PageInfo
//)
//
//// Error Models
//data class ErrorResponse(
//    @SerializedName("timestamp")
//    val timestamp: String,
//    @SerializedName("status")
//    val status: Int,
//    @SerializedName("error")
//    val error: String,
//    @SerializedName("message")
//    val message: String,
//    @SerializedName("path")
//    val path: String
//)
//
//// Pagination Models
//data class PageInfo(
//    @SerializedName("size")
//    val size: Int,
//    @SerializedName("number")
//    val number: Int,
//    @SerializedName("totalElements")
//    val totalElements: Long,
//    @SerializedName("totalPages")
//    val totalPages: Int,
//    @SerializedName("first")
//    val first: Boolean,
//    @SerializedName("last")
//    val last: Boolean
//)
