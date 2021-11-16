package com.svetka.igiftu

import java.time.format.DateTimeFormatter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IgiftuApplication

fun main(args: Array<String>) {
	runApplication<IgiftuApplication>(*args)
}

public val dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
