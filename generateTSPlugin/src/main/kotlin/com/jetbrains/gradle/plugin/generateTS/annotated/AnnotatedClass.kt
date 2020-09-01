package com.jetbrains.gradle.plugin.generateTS.annotated

import com.jetbrains.gradle.plugin.generateTS.ClassReturnType
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

open class AnnotatedClass(cls: KClass<*>) {
    val name: String = cls.simpleName ?: ""
    val returnType: ClassReturnType = getReturnParam(cls)

    private fun getReturnParam(cls: KClass<*>): ClassReturnType {
        return ClassReturnType(cls.superclasses[0])
    }

    override fun toString(): String {
        return "AnnotatedClass(name='$name', returnType=$returnType)"
    }
}