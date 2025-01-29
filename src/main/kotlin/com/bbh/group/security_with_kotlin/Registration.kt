package com.bbh.group.security_with_kotlin

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.MessageSource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.util.Locale

class OnRegistrationCompleteEvent(
    val user: User,
    val locale: Locale,
    val appUrl:String,
) : ApplicationEvent(user)

@Component
class RegistrationListener(
    private val userService: AuthService,
    private val mailSender: JavaMailSender,
    private val messageSource: MessageSource,


    ): ApplicationListener<OnRegistrationCompleteEvent> {

    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) = this.confirmRegistration(event)

    private fun confirmRegistration(event: OnRegistrationCompleteEvent) {
        val user:User = event.user
        val token = generateToken()
        userService.createNewVerificationForUser(user, token)
        val recipientAddress:String = user.email
        val subject = "Registration Confirmation"
        val confirmUrl = "${event.appUrl}/confirm-registration?token=$token"
        val message = messageSource.getMessage("SUCCESS", null, event.locale)

        val simpleMessage = SimpleMailMessage().apply {
            setTo(recipientAddress)
            setSubject(subject)
            text = message+"\r\n"+confirmUrl
        }
        mailSender.send(simpleMessage)
    }
}