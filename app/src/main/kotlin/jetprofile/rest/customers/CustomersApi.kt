package jetprofile.rest.customers

import kara.*

@Suppress("unused")
@Controller("application/json")
@Location("/api/customers")
object CustomersApi {

    @Get("/")
    fun details(fewhfiuhef: Person): Person {
        return Person(
                "Denis",
                22,
                false,
                null,
                mapOf(Pair("job", "denis.shtefan@jetbrains.com"))
        )
    }

    @Post("/:cuweferId")
    fun detaefwef(cuweferId: Int, fewhfiuhef: String): String {
        return "efwefwefwefwef"
    }

}
