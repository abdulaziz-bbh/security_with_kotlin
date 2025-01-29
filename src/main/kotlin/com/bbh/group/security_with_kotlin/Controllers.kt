package com.bbh.group.security_with_kotlin

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val service: AuthService){

    @PostMapping("/signup")
    fun signup(servletRequest: HttpServletRequest, @Valid @RequestBody request: UserCreateRequest): UserResponse {
        return service.registration(request, servletRequest)
    }
    @GetMapping("/confirm-registration")
    fun confirmRegistration(request: WebRequest?, @RequestParam("token") token : String) {
        service.confirmRegistration(request, token)
    }
}