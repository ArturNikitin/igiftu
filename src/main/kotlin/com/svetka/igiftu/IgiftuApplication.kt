package com.svetka.igiftu

import com.svetka.igiftu.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class IgiftuApplication

fun main(args: Array<String>) {
	runApplication<IgiftuApplication>(*args)
}
