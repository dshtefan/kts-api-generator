package com.jetbrains.gradle.plugin.generateTS.annotated

data class AnnotatedObject(
    val annotatedClasses: List<AnnotatedClass>,
    val annotatedFunctions: List<AnnotatedFunction>
)