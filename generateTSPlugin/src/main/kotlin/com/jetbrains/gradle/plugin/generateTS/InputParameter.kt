package com.jetbrains.gradle.plugin.generateTS

import kotlin.reflect.KParameter
import kotlin.reflect.KType

data class InputFunctionParameter(val param: KParameter, val name: String?, val type: KType) {
    override fun toString(): String {
        return "InputParameter(name=$name, type=$type)"
    }
}