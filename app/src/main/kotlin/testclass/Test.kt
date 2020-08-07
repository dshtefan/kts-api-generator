package testclass

import com.jetbrains.annotations.GenerateTS
import com.jetbrains.annotations.TestTS
import java.net.URLClassLoader
import java.util.function.Predicate

@GenerateTS
@TestTS
class Test(n: String) {
    val name: String = n

    @GenerateTS
    fun testFun(): String = name

    override fun toString(): String = name
}

class Person {
    var name: String? = null
    var age = 0
    var hasChildren = false
    var tags: List<String>? = null
    var emails: Map<String, String>? = null
}