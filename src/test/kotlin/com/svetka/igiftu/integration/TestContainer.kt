package com.svetka.igiftu.integration

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
class TestContainer {
	@Autowired
	private lateinit var mockMvc: MockMvc

	companion object {
		@Container
		private val postgreDBContainer: MyContainer = MyContainer("postgres:10.7")
			.withDatabaseName("test_igiftu")

		@JvmStatic
		@DynamicPropertySource
		fun properties(registry: DynamicPropertyRegistry) {
			println("ARTUUR ${postgreDBContainer.jdbcUrl}")
			registry.add("spring.datasource.url", postgreDBContainer::getJdbcUrl)
			registry.add("spring.datasource.password", postgreDBContainer::getPassword)
			registry.add("spring.datasource.username", postgreDBContainer::getUsername)
			registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
			registry.add("aws.cred.key") { "1234" }
			registry.add("aws.cred.pass") { "1234" }
			registry.add("spring.mail.password") { "1234" }
			registry.add("spring.social.facebook.appSecret") {"1235"}
		}
	}

	@Test
	fun testContainer() {
		val token = getToken("admin@gmail.com")

		val contentAsString = mockMvc.perform(
			get("/user/1").header("authorization", token)
		).andExpect(status().isOk).andReturn().response.contentAsString

		println(contentAsString)
	}

	protected fun getToken(email: String): String {
		val user = "{\"email\" : \"$email\", \"password\": \"1234567\" }"

		return mockMvc.perform(
			post("/login")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn()
			.response
			.getHeaderValue("authorization")
				as String
	}
}

class MyContainer(private val image: String) : PostgreSQLContainer<MyContainer>(image)