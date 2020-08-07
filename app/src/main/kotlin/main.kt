import com.jetbrains.annotations.GenerateTS
import com.jetbrains.annotations.TestTS
import testclass.Test
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

@ExperimentalStdlibApi
fun main() {
    val tClass = Test::class
    /*val u = tClass.hasAnnotation<TestTS>()
    val i: Collection<KFunction<*>> = tClass.functions//.filter { it.hasAnnotation<TestTS>() }.map { print(it) }
    for (function in i) {
        println(function.annotations.filter { it.annotationClass == GenerateTS::class })
    }*/
    println(tClass.simpleName)
}