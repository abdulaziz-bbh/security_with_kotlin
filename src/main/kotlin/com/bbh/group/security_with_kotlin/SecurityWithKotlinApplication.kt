package com.bbh.group.security_with_kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class SecurityWithKotlinApplication

fun main(args: Array<String>) {
    runApplication<SecurityWithKotlinApplication>(*args)
}
