package com.svetka.igiftu.controller

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest


@WebMvcTest(UserController::class)
internal class UserControllerTest : AbstractControllerTest() {

}