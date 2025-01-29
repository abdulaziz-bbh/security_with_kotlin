package com.bbh.group.security_with_kotlin

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

sealed class GenericException : RuntimeException() {
    abstract fun errorCode(): ErrorCode
    open fun getErrorMessageArgument(): Array<Any>? = null

    fun getErrorMessage(messageResource: ResourceBundleMessageSource): BaseErrorMessage {
        val errorMessage = try {
            messageResource.getMessage(errorCode().name, getErrorMessageArgument(), LocaleContextHolder.getLocale() )
        }catch (e:Exception){
            e.message
        }
        return BaseErrorMessage(errorCode().errorCode, errorMessage)
    }
}

class VerificationTokenNotFoundException : GenericException(){
    override fun errorCode(): ErrorCode = ErrorCode.VERIFICATION_TOKEN_NOT_FOUND
}
class UserAlreadyExistsException : GenericException(){
    override fun errorCode(): ErrorCode = ErrorCode.USER_ALREADY_EXISTS
}
class VerificationTokenExpiredException : GenericException(){
    override fun errorCode(): ErrorCode = ErrorCode.VERIFICATION_TOKEN_EXPIRED
}



@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(GenericException::class)
    fun handleException(e: GenericException): ResponseEntity<BaseErrorMessage> {
        return ResponseEntity.badRequest().body(e.getErrorMessage(messageSource))
    }
}