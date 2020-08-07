package com.jetbrains.gradle.plugin.generateTS

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.util.*

class GenerateTSPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val generateTsTask: Task = project.task(
            Collections.singletonMap(
                Task.TASK_TYPE,
                GenerateTS::class.java
            ), "generateTypeScript"
        )
        for (task in project.tasks) {
            if (task.name.startsWith("compile") && !task.name.startsWith("compileTest")) {
                generateTsTask.dependsOn(task.name)
                generateTsTask.inputs.files(task)
            }
        }
    }
}