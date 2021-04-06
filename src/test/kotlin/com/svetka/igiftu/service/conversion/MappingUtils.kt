package com.svetka.igiftu.service.conversion

import com.svetka.igiftu.config.MapperConfig
import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.impl.DefaultMapperFactory

object MappingUtils {
    private val mapperFactory: MapperFactory
        get() = DefaultMapperFactory.Builder().build()

    fun getMapperFactory(debugMode: Boolean = false): MapperFactory {
        val mapperFactory = mapperFactory
        mapperFactory.converterFactory.apply {
            registerConverter(MapperConfig.UserConverter())
        }

        return mapperFactory
    }
}