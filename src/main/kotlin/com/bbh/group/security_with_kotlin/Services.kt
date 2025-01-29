package com.bbh.group.security_with_kotlin

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.context.request.WebRequest
import java.util.*

interface AuthService {
    fun registration(createRequest: UserCreateRequest, request: HttpServletRequest):UserResponse
    fun confirmRegistration(request: WebRequest?, token:String)
    fun existsByEmail(email: String)
    fun createNewVerificationForUser(user: User, token: String)
}

interface UserService {

    fun saveRegisteredUser(registeredUser: User)
}

@Service
class AuthServiceImpl(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val tokenRepository: VerificationTokenRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val userService: UserService,

    ): AuthService {

    override fun registration(createRequest: UserCreateRequest, request: HttpServletRequest): UserResponse {
        existsByEmail(createRequest.email)
        var user = userMapper.toEntity(createRequest)
        user = userRepository.save(user)
        val appUrl = "http://${request.serverName}:${request.serverPort}/api/v1/auth"
        eventPublisher.publishEvent(OnRegistrationCompleteEvent(user, request.locale, appUrl))
        return userMapper.toDto(user)
    }

    override fun confirmRegistration(request: WebRequest?, token: String) {
        val verificationToken = getVerificationToken(token)
        val user = verificationToken.user
        if (verificationToken.expiryDate.time - Calendar.getInstance().time.time <= 0){
            throw VerificationTokenExpiredException()
        }
        user.enabled = true
        userService.saveRegisteredUser(user)
    }

    override fun existsByEmail(email: String) {
        email.let {
            if(userRepository.existsUserByEmail(it))
                throw UserAlreadyExistsException()
        }
    }

    override fun createNewVerificationForUser(user: User, token: String) {
        tokenRepository.save(VerificationToken(token, user))
    }

    private fun getVerificationToken(token: String): VerificationToken {
        return tokenRepository.findByTokenAndDeletedFalse(token)
            ?: throw VerificationTokenNotFoundException()
    }
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository) : UserService {

    override fun saveRegisteredUser(registeredUser: User){
        userRepository.save(registeredUser)
    }
}