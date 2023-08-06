package com.pickup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PickupApplication

fun main(args: Array<String>) {
    runApplication<PickupApplication>(*args)
}
