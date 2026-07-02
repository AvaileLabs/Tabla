package com.availelabs.tabla

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class TablaServerApplication

fun main(args: Array<String>) {
    runApplication<TablaServerApplication>(*args)
}

@RestController
class GreetingController {

    @GetMapping("/")
    fun root(): String = sayHello("Spring Boot")
}