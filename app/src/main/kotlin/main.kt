import testclass.Test
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

@ExperimentalStdlibApi
fun main() {
    val tClass = Test::class
    println("OUT: $tClass")
    println(tClass.simpleName)
}