package com.availelabs.tabla

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform