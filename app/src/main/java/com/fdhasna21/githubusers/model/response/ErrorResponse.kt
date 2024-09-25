package com.fdhasna21.githubusers.model.response

import com.fdhasna21.githubusers.utility.type.ErrorType

data class ErrorResponse(
    var type : ErrorType? = null,
    var code : Int? = null,
    var flag : String? = null
)