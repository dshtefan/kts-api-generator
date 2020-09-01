package com.jetbrains.gradle.plugin.generateTS

import kotlin.reflect.KClass
import kotlin.reflect.KType

interface ReturnType {
    val returnType: Any
}

data class ClassReturnType(override val returnType: KClass<*>): ReturnType

data class FunctionReturnType(override val returnType: KType): ReturnType