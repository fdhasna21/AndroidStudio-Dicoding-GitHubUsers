package com.fdhasna21.githubusers.resolver.dataclass

import com.fdhasna21.githubusers.resolver.enumclass.ErrorType

data class ErrorResponse(
    var type : ErrorType? = null,
    var code : Int? = null,
    var flag : String? = null
)
