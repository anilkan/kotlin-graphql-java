package xyz.anilkan

import graphql.ExecutionInput
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
        val result =
            if (req.variables == null) graphQLSchema.execute(ExecutionInput.newExecutionInput().query(req.query))
            else graphQLSchema.execute(ExecutionInput.newExecutionInput().query(req.query).variables(req.variables))

        call.respond(result)

    }
}