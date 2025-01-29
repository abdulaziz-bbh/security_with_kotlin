package com.bbh.group.security_with_kotlin

import java.util.*

fun generateToken(): String {
    return UUID.randomUUID().toString()
}