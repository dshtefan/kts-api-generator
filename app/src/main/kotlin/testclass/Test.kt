package testclass

class Test(override val name: String): Name {

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

interface Name {
    val name: String
}