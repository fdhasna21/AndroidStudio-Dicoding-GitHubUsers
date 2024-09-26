package com.fdhasna21.githubusers.model.response

import com.fdhasna21.githubusers.utility.type.ErrorType

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
data class ErrorResponse(
    var type : ErrorType? = null,
    var code : Int? = null,
    var flag : String? = null
) :BaseResponse()