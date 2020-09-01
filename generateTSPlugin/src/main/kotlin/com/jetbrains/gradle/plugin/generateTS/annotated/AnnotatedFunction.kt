package com.jetbrains.gradle.plugin.generateTS.annotated

import com.jetbrains.gradle.plugin.generateTS.FunctionReturnType
import com.jetbrains.gradle.plugin.generateTS.InputFunctionParameter
import kotlin.reflect.KFunction
import kotlin.reflect.full.valueParameters

open class AnnotatedFunction(fn: KFunction<*>) {
    val name: String = fn.name
    val inputParameters: List<InputFunctionParameter> = getInputParams(fn)
    val returnType: FunctionReturnType = getReturnParam(fn)

    private fun getInputParams(fn: KFunction<*>): List<InputFunctionParameter> {
        return fn.valueParameters.map { InputFunctionParameter(it, it.name, it.type) }
    }

    private fun getReturnParam(fn: KFunction<*>): FunctionReturnType {
        return FunctionReturnType(fn.returnType)
    }

    override fun toString(): String {
        return "AnnotatedFunction(name='$name', inputParameters=$inputParameters, returnType=$returnType)"
    }
}