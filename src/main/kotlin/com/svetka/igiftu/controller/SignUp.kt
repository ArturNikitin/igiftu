package com.svetka.igiftu.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.web.ProviderSignInController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/user/signin/")
class SignUp {

	@Autowired
	lateinit var controller: ProviderSignInController

	@GetMapping("/{providerId}")
	fun test(@PathVariable providerId: String, request : NativeWebRequest): RedirectView? {
		return controller.signIn(providerId, request)
	}
}