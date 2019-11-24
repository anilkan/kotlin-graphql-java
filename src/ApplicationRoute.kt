package xyz.anilkan

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

data class GraphQLRequest(
    val query: String = "",
    val operationName: String? = null,
    val variables: Map<String, Any>? = emptyMap()
)

fun Routing.graphql () {
    get("/hello") {
        call.respondText("Hello World")
    }

    post("/graphql") {
        val req = call.receive<GraphQLRequest>()
        val query = req.query
        val variables = if (req.variables == null) null
        else jacksonObjectMapper().writeValueAsString(req.variables)

        call.respond(graphQLSchema.execute(query))
//        val res = schema.runCatching {
//            execute(query, variables)
//        }
//
//        val response = res.fold(onSuccess = { it }, onFailure = {
//            """
//                {
//                    "errors": "${it.message}"
//                }
//            """.trimIndent()
//        })
//
//        call.respond(response)
    }
}