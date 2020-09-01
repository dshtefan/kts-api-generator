package com.jetbrains.gradle.plugin.generateTS

import com.jetbrains.gradle.plugin.generateTS.annotated.AnnotatedClass
import com.jetbrains.gradle.plugin.generateTS.annotated.AnnotatedFunction
import com.jetbrains.gradle.plugin.generateTS.annotated.AnnotatedObject
import kara.internal.getKaraAnnotation
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

fun getRoutesFromClass(clazz: KClass<*>): AnnotatedObject? {
    val annClasses = getAnnotatedClasses(clazz)
    val annFunctions = getAnnotatedFunctions(clazz)
    return if (annClasses.isNotEmpty() || annFunctions.isNotEmpty())
        AnnotatedObject(annClasses, annFunctions)
    else
        null
}

fun getAnnotatedClasses(clazz: KClass<*>): List<AnnotatedClass> {
    return clazz.nestedClasses.filter { hasRouteAnnotation(it) }. map { AnnotatedClass(it) }
}

fun getAnnotatedFunctions(clazz: KClass<*>): List<AnnotatedFunction> {
    return clazz.declaredFunctions.filter { hasRouteAnnotation(it) }.map { AnnotatedFunction(it) }
}

fun hasRouteAnnotation(obj: KAnnotatedElement) : Boolean {
    return obj.getKaraAnnotation() != null
}