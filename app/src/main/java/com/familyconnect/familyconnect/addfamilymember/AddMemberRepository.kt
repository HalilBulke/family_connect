package com.familyconnect.familyconnect.addfamilymember

import retrofit2.Response
import javax.inject.Inject

interface AddFamilyMemberRepository {
    suspend fun addFamilyMember(request: AddFamilyMemberRequest): Response<AddFamilyMemberResponse>
}

class AddFamilyMemberRepositoryImpl @Inject constructor(
    private val addMemberApiService: AddMemberApiService
) : AddFamilyMemberRepository {

    override suspend fun addFamilyMember(request: AddFamilyMemberRequest): Response<AddFamilyMemberResponse> {
        return addMemberApiService.addFamilyMember(request.familyId, request.userNames)
    }
}


// Request body data class
data class AddFamilyMemberRequest(
    val familyId: Int,
    val userNames: List<String>
)

// Response data class
data class AddFamilyMemberResponse(
    val success: Boolean,
    val message: String?,
    val family: Family? // Assuming you'll receive the updated family data
)

data class Family(
    val id: Int, // Change the type according to the actual type in the response
    val familyName: String,
    val familyMembers: List<String>, // Change the type according to the actual type in the response
    val creatorUserName: String
)



