package com.jetbrains.aprocessor

import com.google.auto.service.AutoService
import com.jetbrains.annotations.GenerateTS
import com.jetbrains.annotations.GenerateTSAll
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {
    private val annotationMap: Map<Class<out Annotation>,List<ElementKind>> = hashMapOf(
            GenerateTS::class.java to listOf(ElementKind.METHOD, ElementKind.CLASS),
            GenerateTSAll::class.java to listOf(ElementKind.CLASS)
    )

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenerateTS::class.java.name, GenerateTSAll::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        annotationMap.forEach { (annName, supportedElKind) -> run {
            roundEnv.getElementsAnnotatedWith(annName)
                    .forEach {
                        if (it.kind !in supportedElKind) {
                            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "This ElementKind is not supported.")
                            return true
                        }
                        println("__NAME: " + it.simpleName.toString())
                        processAnnotation(it)
                    }
        }}

        return false
    }

    private fun processAnnotation(element: Element) {
    }
}