package com.jetbrains.gradle.plugin.generateTS

import com.jetbrains.gradle.plugin.generateTS.annotated.AnnotatedObject
import cz.habarta.typescript.generator.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet
import kotlin.reflect.*

abstract class GenerateTS : DefaultTask() {
    var outputFile: String? = null
    var outputFileType: TypeScriptFileType? = null
    var outputKind: TypeScriptOutputKind? = null
    var classes: List<String>? = null
    var jsonLibrary: JsonLibrary? = null
    var loggingLevel: Logger.Level? = null

    @TaskAction
    @Throws(java.lang.Exception::class)
    fun generate() {
        if (outputKind == null) {
            outputKind = TypeScriptOutputKind.module
        }
        if (jsonLibrary == null) {
            jsonLibrary = JsonLibrary.jackson2
        }
        TypeScriptGenerator.setLogger(Logger(loggingLevel))

        val urls: MutableSet<java.net.URL> = LinkedHashSet<java.net.URL>()
        for (task in project.tasks) {
            if (task.name.startsWith("compile") && !task.name.startsWith("compileTest")) {
                for (file in task.outputs.files) {
                    urls.add(file.toURI().toURL())
                }
            }
        }
        urls.addAll(getFilesFromConfiguration("compile"))
        urls.addAll(getFilesFromConfiguration("compileClasspath"))
        Settings.createClassLoader(
            project.name,
            urls.toTypedArray(),
            Thread.currentThread().contextClassLoader
        ).use { classLoader ->
            val settings = Settings()
            val parameters: Input.Parameters = Input.Parameters()
            val output: java.io.File = if (outputFile != null) project.file(outputFile) else java.io.File(
                java.io.File(
                    project.buildDir,
                    "typescript-generator"
                ), project.name + settings.extension
            )

            val annObjects: List<AnnotatedObject> = loadAnnotatedObjects(classes, classLoader)

            val returnClasses = getReturnClasses(annObjects) + getInputClasses(annObjects)

            settings.outputKind = TypeScriptOutputKind.module
            settings.jsonLibrary = JsonLibrary.jackson2
            settings.outputFileType = outputFileType
            parameters.classLoader = classLoader
            parameters.classNames = returnClasses
            settings.validateFileName(output)
            TypeScriptGenerator(settings).generateTypeScript(Input.from(parameters), Output.to(output))
        }
    }

    private fun loadAnnotatedObjects(classNames: List<String?>?, classLoader: URLClassLoader): List<AnnotatedObject> {
        return loadClasses(classNames!!, classLoader)!!.mapNotNull { getRoutesFromClass(it) }
    }

    private fun loadClasses(classNames: List<String?>, classLoader: URLClassLoader): List<KClass<*>>? {
        val classes: MutableList<KClass<*>> = ArrayList()
        classNames.forEach { cl ->
            run {
                val clazz: KClass<*> = Class.forName(cl, true, classLoader).kotlin
                classes.add(clazz)
            }
        }
        return classes
    }

    private fun isKotlinClass(className: String): Boolean {
        return className.substringBefore('.') == "kotlin"
    }

    private fun getReturnClasses(annObjects: List<AnnotatedObject>): ArrayList<String> {
        val returnClasses: ArrayList<String> = ArrayList()

        annObjects.forEach { obj ->
            obj.annotatedFunctions.forEach { ann ->
                run {
                    val rt = ann.returnType.returnType.toString()
                    if (isKotlinClass(rt)) return@run
                    returnClasses.add(rt)
                }
            }
        }
        return returnClasses
    }

    private fun getInputClasses(annObjects: List<AnnotatedObject>): ArrayList<String> {
        val inputClasses: ArrayList<String> = ArrayList()
        annObjects.forEach { obj ->
            obj.annotatedFunctions.forEach {
                it.inputParameters.forEach{ ip ->
                    run {
                        if (isKotlinClass(ip.type.toString())) return@run
                        inputClasses.add(ip.type.toString())
                    }
                }
            }
        }
        return inputClasses
    }

    private fun getFilesFromConfiguration(configuration: String): List<java.net.URL> {
        return try {
            val urls: MutableList<java.net.URL> = java.util.ArrayList()
            for (file in project.configurations.getAt(configuration).files) {
                urls.add(file.toURI().toURL())
            }
            urls
        } catch (e: java.lang.Exception) {
            TypeScriptGenerator.getLogger().warning(
                String.format(
                    "Cannot get file names from configuration '%s': %s",
                    configuration,
                    e.message
                )
            )
            emptyList<java.net.URL>()
        }
    }
}