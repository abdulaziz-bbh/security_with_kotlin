package com.bbh.group.security_with_kotlin

enum class ErrorCode(val errorCode: Int) {

    USER_ALREADY_EXISTS(10),
    VERIFICATION_TOKEN_NOT_FOUND(100),
    VERIFICATION_TOKEN_EXPIRED(110),

}

