package com.bbh.group.security_with_kotlin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig{

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{it.disable()}
            .authorizeHttpRequests{it
                .requestMatchers("api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Configuration
class AppConfig{

    @Bean
    fun localeResolver() = SessionLocaleResolver().apply {setDefaultLocale(Locale.getDefault())}

    @Bean
    fun messageSource() = ResourceBundleMessageSource().apply {
        setDefaultEncoding(Charsets.UTF_8.name())
        setBasename("message")
    }
}

@Configuration
@ConfigurationProperties(prefix = "email")
data class EmailProviderConfiguration(
    var host: String? = null,
    var port: Int? = null,
    var username: String? = null,
    var password: String? = null,
    var debug: Boolean? = null
) {
    companion object {
        val DEFAULT_CHARSET: Charset = StandardCharsets.UTF_8
    }
}

@Configuration
class EmailConfig(private val providerConfiguration: EmailProviderConfiguration) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val javaMailSender = JavaMailSenderImpl()

        javaMailSender.host = providerConfiguration.host
        javaMailSender.port = providerConfiguration.port ?: 0
        javaMailSender.password = providerConfiguration.password
        javaMailSender.username = providerConfiguration.username

        val properties = javaMailSender.javaMailProperties
        properties["mail.transport.protocol"] = "smtp"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"

        // Convert Boolean? to String for mail.debug property
        providerConfiguration.debug?.let {
            properties["mail.debug"] = it.toString()
        }

        return javaMailSender
    }
}
