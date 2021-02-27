package com.svetka.igiftu.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class TestController {

    @GetMapping("/test")
    fun test(): String = "Jello montachello"
}