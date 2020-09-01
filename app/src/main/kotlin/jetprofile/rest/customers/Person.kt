package jetprofile.rest.customers

class Person(
    val name: String,
    val age: Int,
    val hasChildren: Boolean,
    val tags: List<String>?,
    override val emails: Map<String, String>?
): Employee

interface Employee {
    val emails: Map<String, String>?
}