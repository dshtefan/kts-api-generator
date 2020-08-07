package com.jetbrains.gradle.plugin.generateTS

import cz.habarta.typescript.generator.ClassMapping
import cz.habarta.typescript.generator.DateMapping
import cz.habarta.typescript.generator.EnumMapping
import cz.habarta.typescript.generator.GsonConfiguration
import cz.habarta.typescript.generator.Input
import cz.habarta.typescript.generator.Jackson2Configuration
import cz.habarta.typescript.generator.JsonLibrary
import cz.habarta.typescript.generator.JsonbConfiguration
import cz.habarta.typescript.generator.Logger
import cz.habarta.typescript.generator.ModuleDependency
import cz.habarta.typescript.generator.NullabilityDefinition
import cz.habarta.typescript.generator.OptionalProperties
import cz.habarta.typescript.generator.OptionalPropertiesDeclaration
import cz.habarta.typescript.generator.Output
import cz.habarta.typescript.generator.RestNamespacing
import cz.habarta.typescript.generator.Settings
import cz.habarta.typescript.generator.StringQuotes
import cz.habarta.typescript.generator.TypeScriptFileType
import cz.habarta.typescript.generator.TypeScriptGenerator
import cz.habarta.typescript.generator.TypeScriptOutputKind
import cz.habarta.typescript.generator.util.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader

abstract class GenerateTS : DefaultTask() {
    var outputFile: String? = null
    var outputFileType: TypeScriptFileType? = null
    var outputKind: TypeScriptOutputKind? = null
    var module: String? = null
    var namespace: String? = null
    var mapPackagesToNamespaces = false
    var umdNamespace: String? = null
    var moduleDependencies: List<ModuleDependency>? = null
    var classes: List<String>? = null
    var classPatterns: List<String>? = null
    var classesWithAnnotations: List<String>? = null
    var classesImplementingInterfaces: List<String>? = null
    var classesExtendingClasses: List<String>? = null
    var classesFromJaxrsApplication: String? = null
    var classesFromAutomaticJaxrsApplication = false
    var excludeClasses: List<String>? = null
    var excludeClassPatterns: List<String>? = null
    var includePropertyAnnotations: List<String>? = null
    var excludePropertyAnnotations: List<String>? = null
    var jsonLibrary: JsonLibrary? = null
    var jackson2Configuration: Jackson2Configuration? = null
    var gsonConfiguration: GsonConfiguration? = null
    var jsonbConfiguration: JsonbConfiguration? = null
    var optionalProperties: OptionalProperties? = null
    var optionalPropertiesDeclaration: OptionalPropertiesDeclaration? = null
    var nullabilityDefinition: NullabilityDefinition? = null
    var declarePropertiesAsReadOnly = false
    var removeTypeNamePrefix: String? = null
    var removeTypeNameSuffix: String? = null
    var addTypeNamePrefix: String? = null
    var addTypeNameSuffix: String? = null
    var customTypeNaming: List<String>? = null
    var customTypeNamingFunction: String? = null
    var referencedFiles: List<String>? = null
    var importDeclarations: List<String>? = null
    var customTypeMappings: List<String>? = null
    var customTypeAliases: List<String>? = null
    var mapDate: DateMapping? = null
    var mapEnum: EnumMapping? = null
    var nonConstEnums = false
    var nonConstEnumAnnotations: List<String>? = null
    var mapClasses: ClassMapping? = null
    var mapClassesAsClassesPatterns: List<String>? = null
    var generateConstructors = false
    var disableTaggedUnions = false
    var ignoreSwaggerAnnotations = false
    var generateJaxrsApplicationInterface = false
    var generateJaxrsApplicationClient = false
    var generateSpringApplicationInterface = false
    var generateSpringApplicationClient = false
    var scanSpringApplication = false
    var restNamespacing: RestNamespacing? = null
    var restNamespacingAnnotation: String? = null
    var restResponseType: String? = null
    var restOptionsType: String? = null
    var customTypeProcessor: String? = null
    var sortDeclarations = false
    var sortTypeDeclarations = false
    var noFileComment = false
    var noTslintDisable = false
    var noEslintDisable = false
    var tsNoCheck = false
    var javadocXmlFiles: List<java.io.File>? = null
    var extensionClasses: List<String>? = null
    var extensions: List<String>? = null
    var extensionsWithConfiguration: List<Settings.ConfiguredExtension>? = null
    var optionalAnnotations: List<String>? = null
    var requiredAnnotations: List<String>? = null
    var nullableAnnotations: List<String>? = null
    var generateInfoJson = false
    var generateNpmPackageJson = false
    var npmName: String? = null
    var npmVersion: String? = null
    var npmBuildScript: String? = null
    var stringQuotes: StringQuotes? = null
    var indentString: String? = null
    var jackson2ModuleDiscovery = false
    var jackson2Modules: List<String>? = null
    var loggingLevel: Logger.Level? = null
    private fun createSettings(classLoader: URLClassLoader): Settings {
        val settings = Settings()
        if (outputFileType != null) {
            settings.outputFileType = outputFileType
        }
        settings.outputKind = outputKind
        settings.module = module
        settings.namespace = namespace
        settings.mapPackagesToNamespaces = mapPackagesToNamespaces
        settings.umdNamespace = umdNamespace
        settings.moduleDependencies = moduleDependencies
        settings.setExcludeFilter(excludeClasses, excludeClassPatterns)
        settings.jsonLibrary = jsonLibrary
        settings.setJackson2Configuration(classLoader, jackson2Configuration)
        settings.gsonConfiguration = gsonConfiguration
        settings.jsonbConfiguration = jsonbConfiguration
        settings.optionalProperties = optionalProperties
        settings.optionalPropertiesDeclaration = optionalPropertiesDeclaration
        settings.nullabilityDefinition = nullabilityDefinition
        settings.declarePropertiesAsReadOnly = declarePropertiesAsReadOnly
        settings.removeTypeNamePrefix = removeTypeNamePrefix
        settings.removeTypeNameSuffix = removeTypeNameSuffix
        settings.addTypeNamePrefix = addTypeNamePrefix
        settings.addTypeNameSuffix = addTypeNameSuffix
        settings.customTypeNaming = Settings.convertToMap(customTypeNaming)
        settings.customTypeNamingFunction = customTypeNamingFunction
        settings.referencedFiles = referencedFiles
        settings.importDeclarations = importDeclarations
        settings.customTypeMappings = Settings.convertToMap(customTypeMappings)
        settings.customTypeAliases = Settings.convertToMap(customTypeAliases)
        settings.mapDate = mapDate
        settings.mapEnum = mapEnum
        settings.nonConstEnums = nonConstEnums
        settings.loadNonConstEnumAnnotations(classLoader, nonConstEnumAnnotations)
        settings.mapClasses = mapClasses
        settings.mapClassesAsClassesPatterns = mapClassesAsClassesPatterns
        settings.generateConstructors = generateConstructors
        settings.disableTaggedUnions = disableTaggedUnions
        settings.ignoreSwaggerAnnotations = ignoreSwaggerAnnotations
        settings.generateJaxrsApplicationInterface = generateJaxrsApplicationInterface
        settings.generateJaxrsApplicationClient = generateJaxrsApplicationClient
        settings.generateSpringApplicationInterface = generateSpringApplicationInterface
        settings.generateSpringApplicationClient = generateSpringApplicationClient
        settings.scanSpringApplication = scanSpringApplication
        settings.restNamespacing = restNamespacing
        settings.setRestNamespacingAnnotation(classLoader, restNamespacingAnnotation)
        settings.restResponseType = restResponseType
        settings.setRestOptionsType(restOptionsType)
        settings.loadCustomTypeProcessor(classLoader, customTypeProcessor)
        settings.sortDeclarations = sortDeclarations
        settings.sortTypeDeclarations = sortTypeDeclarations
        settings.noFileComment = noFileComment
        settings.noTslintDisable = noTslintDisable
        settings.noEslintDisable = noEslintDisable
        settings.tsNoCheck = tsNoCheck
        settings.javadocXmlFiles = javadocXmlFiles
        settings.loadExtensions(classLoader, Utils.concat(extensionClasses, extensions), extensionsWithConfiguration)
        settings.loadIncludePropertyAnnotations(classLoader, includePropertyAnnotations)
        settings.loadExcludePropertyAnnotations(classLoader, excludePropertyAnnotations)
        settings.loadOptionalAnnotations(classLoader, optionalAnnotations)
        settings.loadRequiredAnnotations(classLoader, requiredAnnotations)
        settings.loadNullableAnnotations(classLoader, nullableAnnotations)
        settings.generateInfoJson = generateInfoJson
        settings.generateNpmPackageJson = generateNpmPackageJson
        settings.npmName = if (npmName == null && generateNpmPackageJson) project.name else npmName
        settings.npmVersion =
            if (npmVersion == null && generateNpmPackageJson) settings.defaultNpmVersion else npmVersion
        settings.npmBuildScript = npmBuildScript
        settings.setStringQuotes(stringQuotes)
        settings.setIndentString(indentString)
        settings.jackson2ModuleDiscovery = jackson2ModuleDiscovery
        settings.loadJackson2Modules(classLoader, jackson2Modules)
        settings.classLoader = classLoader
        return settings
    }

    @TaskAction
    @Throws(java.lang.Exception::class)
    fun generate() {
        if (outputKind == null) {
            throw RuntimeException("Please specify 'outputKind' property.")
        }
        if (jsonLibrary == null) {
            throw RuntimeException("Please specify 'jsonLibrary' property.")
        }
        TypeScriptGenerator.setLogger(Logger(loggingLevel))
        TypeScriptGenerator.printVersion()

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
            val settings: Settings = createSettings(classLoader)
            val parameters: Input.Parameters = Input.Parameters()
            //println("SIZE: " + urls.size)
            //urls.forEach { x -> "|||___  " + println(x) }
            //println("CLASSES: ${classes}")
            //println(Thread.currentThread().contextClassLoader.loadClass(classes?.get(0)))
            parameters.classNames = classes
            parameters.classNamePatterns = classPatterns
            parameters.classesWithAnnotations = classesWithAnnotations
            parameters.classesImplementingInterfaces = classesImplementingInterfaces
            parameters.classesExtendingClasses = classesExtendingClasses
            parameters.jaxrsApplicationClassName = classesFromJaxrsApplication
            parameters.automaticJaxrsApplication = classesFromAutomaticJaxrsApplication
            parameters.isClassNameExcluded = settings.excludeFilter
            parameters.classLoader = classLoader
            parameters.debug = loggingLevel === Logger.Level.Debug
            val output: java.io.File = if (outputFile != null) project.file(outputFile) else java.io.File(
                java.io.File(
                    project.buildDir,
                    "typescript-generator"
                ), project.name + settings.extension
            )
            settings.validateFileName(output)
            //println("SOURCE TYPES: " + Input.from(parameters).sourceTypes.forEach { x -> print(x.javaClass.name + ", ") })
            TypeScriptGenerator(settings).generateTypeScript(Input.from(parameters), Output.to(output))
        }
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