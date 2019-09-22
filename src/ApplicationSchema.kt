package xyz.anilkan

import graphql.GraphQL
import graphql.Scalars.*
import graphql.language.Field
import graphql.schema.*
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.GraphQLArgument.newArgument
import graphql.schema.GraphQLCodeRegistry.newCodeRegistry
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import xyz.anilkan.model.Safe
import xyz.anilkan.repository.SafeRepository
import xyz.anilkan.repository.Safes


val schema : String = "type Query{hello: String}"

val typeDefinitionRegistry = SchemaParser().parse(schema)

val runtimeWiring = RuntimeWiring.newRuntimeWiring()
    .type("Query", { t -> t.dataFetcher("hello", StaticDataFetcher("world")) })
    .build()

val graphQLSchema =
    GraphQL.newGraphQL(SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring))
        .build()

val safeType = newObject()
    .name("Safe")
    .field(GraphQLFieldDefinition.newFieldDefinition()
        .name("id")
        .type(GraphQLInt))
    .field(GraphQLFieldDefinition.newFieldDefinition()
        .name("code")
        .type(GraphQLString))
    .field(GraphQLFieldDefinition.newFieldDefinition()
        .name("name")
        .type(GraphQLString))
    .field(GraphQLFieldDefinition.newFieldDefinition()
        .name("balance")
        .type(GraphQLBigDecimal))
    .build()

val queryType = newObject()
    .name("QueryType")
    .field(newFieldDefinition()
        .name("Safe")
        .type(safeType)
        .argument(newArgument()
            .name("id")
            .type(GraphQLInt)))
    .build()

var safeDataFetcher = object : DataFetcher<Safe> {
    override fun get(environment: DataFetchingEnvironment): Safe {
        // TODO : Gereksiz şeyler

        val selectedTableFields = Safes.columns.filter {
                t -> environment.selectionSet.fields.any() { c -> c.name == t.name }
        }

        return SafeRepository.getElement(environment.getArgument("id"), selectedTableFields)
    }
}

var codeRegistry = newCodeRegistry()
    .dataFetcher(
        FieldCoordinates.coordinates("QueryType", "Safe"), safeDataFetcher
    )
    .build()

var graphQLSchemaIki = GraphQL.newGraphQL(
    GraphQLSchema.newSchema()
        .query(queryType)
        .codeRegistry(codeRegistry)
        .build()
).build()