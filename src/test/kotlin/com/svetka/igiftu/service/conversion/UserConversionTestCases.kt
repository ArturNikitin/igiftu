package com.svetka.igiftu.service.conversion

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.impl.DefaultMapperFactory
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class UserConversionTestCases {

    @Test
    fun userToUserDto() {
        val factory: MapperFactory = MappingUtil.getMapperFactory(false)
        val mapper = factory.mapperFacade

        val map = mapper.map(getUser(), UserDto::class.java)

        println(map)
    }

    private fun getUser() = User(
        1L,
        LocalDateTime.now(),
        "1234",
        "email@gmail.com",
        "login",
        UserRoles.ROLE_USER,
        isEnabled = true,
        isAccountNonLocked = true
    )
}

object MappingUtil {
    /**
     * Set this system property to true (typically during a build) to make sure no unit
     * tests are inadvertently using debug mode (EclipseJdtCompilerStrategy); we have a
     * special TestSuite which re-runs all unit tests using EclipseJdtCompilerStrategy,
     * so if any unit tests are explicitly using it, they will prevent testing with
     * JavassistCompilerStrategy
     */
    const val DISABLE_DEBUG_MODE = "ma.glasnost.orika.test.MappingUtil.noDebug"

    /**
     * @return a new default instance of MapperFactory
     */
    private val mapperFactory: MapperFactory
        get() = DefaultMapperFactory.Builder().build()

    /**
     * @return a new default instance of MapperFactory, with the specified debug
     * mode configuration.
     * @param debugMode if true, EclipseJdt will be used for the compiler
     * strategy (for step-debugging in IDEs), and class and source files will be written to disk.
     */
    fun getMapperFactory(debugMode: Boolean): MapperFactory {
        return if (debugMode) {
            if (java.lang.Boolean.valueOf(System.getProperty(DISABLE_DEBUG_MODE))) {
                mapperFactory
            } else {
                DefaultMapperFactory.Builder().compilerStrategy(EclipseJdtCompilerStrategy()).build()
            }
        } else {
            mapperFactory
        }
    }
}