package com.fdhasna21.githubusers.model

import com.fdhasna21.githubusers.utility.type.ErrorType

data class ErrorResponse(
    var type : ErrorType? = null,
    var code : Int? = null,
    var flag : String? = null
)