package xyz.anilkan

import graphql.GraphQL
import graphql.schema.DataFetcher
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import org.joda.time.DateTime
import xyz.anilkan.model.*
import java.io.File

var safeDataFetcher = DataFetcher<Safe> { environment ->
    SafeRepository.find(environment.getArgument("id"))
}

var firmDataFetcher = DataFetcher<Firm> { environment ->
    FirmRepository.find(environment.getArgument("id"))
}

var expenseDataFetcher = DataFetcher<Expense> { environment ->
    MovementRepository.find(environment.getArgument("id")) as Expense
}

var createSafeDataFetcher = DataFetcher<Safe> { environment ->
    val safe: Safe = Safe(
        0,
        environment.getArgument("code"),
        environment.getArgument("name"),
        environment.getArgument("balance")
    )
    SafeRepository.find(SafeRepository.add(safe))
}

var createFirmDataFetcher = DataFetcher<Firm> { environment ->
    val firm: Firm = Firm(
        0,
        environment.getArgument("name")
    )
    FirmRepository.find(FirmRepository.add(firm))
}

var createExpenseDataFetcher = DataFetcher<Expense> { environment ->
    val safeMap = environment.getArgument("from") as Map<String, Object>
    val firmMap = environment.getArgument("to") as Map<String, Object>

    val safeId: Int = if (safeMap["id"] != null) safeMap["id"] as Int
    else SafeRepository.add(Safe(0, safeMap["code"] as String, safeMap["name"] as String, safeMap["balance"] as Double))

    val firmId: Int = if (firmMap["id"] != null) safeMap["id"] as Int
    else FirmRepository.add(Firm(0, firmMap["name"] as String))

    val expense: Expense = Expense(
        0,
        DateTime.now(),
        safeId,
        firmId
    )

    MovementRepository.find(MovementRepository.add(expense)) as Expense
}

val schema : String = File("/home/anil/IdeaProjects/ktor-java-graphql/resources/schema.graphql").readText(Charsets.UTF_8)

val typeDefinitionRegistry: TypeDefinitionRegistry = SchemaParser().parse(schema)

val runtimeWiring: RuntimeWiring = RuntimeWiring.newRuntimeWiring()
    .type("Query") { t -> t.dataFetcher("hello", StaticDataFetcher("world")) }
    .type("Query") {t -> t.dataFetcher("Safe", safeDataFetcher)}
    .type("Query") {t-> t.dataFetcher("Firm", firmDataFetcher)}
    .type("Query") { t -> t.dataFetcher("Expense", expenseDataFetcher) }
    .type("Mutation") { t -> t.dataFetcher("createSafe", createSafeDataFetcher) }
    .type("Mutation") { t -> t.dataFetcher("createFirm", createFirmDataFetcher) }
    .type("Mutation") { t -> t.dataFetcher("createExpense", createExpenseDataFetcher) }
    .build()

val graphQLSchema: GraphQL =
    GraphQL.newGraphQL(SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring))
        .build()