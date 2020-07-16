import com.jetbrains.annotations.GenerateTS
import com.jetbrains.annotations.TestTS

@GenerateTS
@TestTS
class Test(n: String) {
    private val name: String = n

    @GenerateTS
    fun testFun(): String = name

    override fun toString(): String = name
}