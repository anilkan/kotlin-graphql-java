package xyz.anilkan

import graphql.GraphQL
import graphql.Scalars.*
import graphql.schema.*
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.GraphQLArgument.newArgument
import graphql.schema.GraphQLCodeRegistry.newCodeRegistry
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.idl.TypeDefinitionRegistry
import xyz.anilkan.model.*
import java.io.File

/*
const val schema : String = "type Query{hello: String}"

val typeDefinitionRegistry: TypeDefinitionRegistry = SchemaParser().parse(schema)

val runtimeWiring: RuntimeWiring = RuntimeWiring.newRuntimeWiring()
    .type("Query") { t -> t.dataFetcher("hello", StaticDataFetcher("world")) }
    .build()

val graphQLSchema: GraphQL =
    GraphQL.newGraphQL(SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring))
        .build()
*/
val safeType: GraphQLObjectType = newObject()
    .name("Safe")
    .field(
        newFieldDefinition()
        .name("id")
        .type(GraphQLInt))
    .field(
        newFieldDefinition()
        .name("code")
        .type(GraphQLString))
    .field(
        newFieldDefinition()
        .name("name")
        .type(GraphQLString))
    .field(
        newFieldDefinition()
        .name("balance")
        .type(GraphQLBigDecimal))
    .build()

val safeQueryType: GraphQLObjectType = newObject()
    .name("QueryType")
    .field(newFieldDefinition()
        .name("Safe")
        .type(safeType)
        .argument(newArgument()
            .name("id")
            .type(GraphQLInt)))
    .build()

var safeDataFetcher = DataFetcher<Safe> { environment ->
    // TODO : Gereksiz şeyler

    val selectedTableFields = Safes.columns.filter {
            t -> environment.selectionSet.fields.any() { c -> c.name == t.name }
    }

    SafeRepository.find(environment.getArgument("id"))
}

var safeCodeRegistry: GraphQLCodeRegistry = newCodeRegistry()
    .dataFetcher(
        FieldCoordinates.coordinates("QueryType", "Safe"), safeDataFetcher
    )
    .build()

// Firm
val firmType: GraphQLObjectType = newObject()
    .name("Firm")
    .field(
        newFieldDefinition()
            .name("id")
            .type(GraphQLInt))
    .field(
        newFieldDefinition()
            .name("name")
            .type(GraphQLString))
    .build()

val firmQueryType: GraphQLObjectType = newObject()
    .name("QueryType")
    .field(newFieldDefinition()
        .name("Firm")
        .type(firmType)
        .argument(newArgument()
            .name("id")
            .type(GraphQLInt)))
    .build()

var firmDataFetcher = DataFetcher<Firm> { environment ->
    FirmRepository.find(environment.getArgument("id"))
}

var firmCodeRegistry: GraphQLCodeRegistry = newCodeRegistry()
    .dataFetcher(
        FieldCoordinates.coordinates("QueryType", "Firm"), firmDataFetcher
    )
    .build()


var graphQLSchemaIki: GraphQL = GraphQL.newGraphQL(
    GraphQLSchema.newSchema(
        GraphQLSchema.newSchema()
            .query(safeQueryType)
            .codeRegistry(safeCodeRegistry).build()
    )
        .query(firmQueryType)
        .codeRegistry(firmCodeRegistry)
        .build()
).build()

val schema : String = File("/home/anil/IdeaProjects/ktor-java-graphql/resources/schema.graphql").readText(Charsets.UTF_8)

val typeDefinitionRegistry: TypeDefinitionRegistry = SchemaParser().parse(schema)

val runtimeWiring: RuntimeWiring = RuntimeWiring.newRuntimeWiring()
    .type("Query") { t -> t.dataFetcher("hello", StaticDataFetcher("world")) }
    .type("Query") {t -> t.dataFetcher("Safe", safeDataFetcher)}
    .type("Query") {t-> t.dataFetcher("Firm", firmDataFetcher)}
    .build()

val graphQLSchema: GraphQL =
    GraphQL.newGraphQL(SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring))
        .build()