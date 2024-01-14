package server

import com.zachary_moore.ktql.KTQLEngine
import com.zachary_moore.ktql.ktql
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientServerShould {

    private lateinit var engine: KTQLEngine<ExecutionResult>

    @BeforeAll
    fun setup() {
        val schema = this::class.java.classLoader.getResource("schema.graphqls").readText()
        val schemaParser = SchemaParser()
        val typeDefinitionRegistry = schemaParser.parse(schema)
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query") { builder ->
                builder.dataFetcher("TweetsMeta", StaticDataFetcher(mapOf("count" to 5)))
                builder.dataFetcher("Tweet"
                ) { environment ->
                    environment?.arguments?.let { args ->
                        val id = args["id"]
                        mapOf(
                            "id" to id,
                            "body" to "This is a tweet"
                        )
                    } ?: mapOf<String, Any?>()
                }
            }
            .scalar(GraphQLScalarType.newScalar()
                .name("Date")
                .coercing(object : Coercing<Any, Any> {
                }).build())
            .scalar(GraphQLScalarType.newScalar()
                .name("Url")
                .coercing(object : Coercing<Any, Any> {
                }).build())
            .build()
        val schemaGenerator = SchemaGenerator()
        val gqlSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
        val graphQL = GraphQL.newGraphQL(gqlSchema).build()
        engine = KTQLEngine { query -> CompletableFuture.completedFuture(graphQL.execute(query)) }
    }


    @Test
    fun simpleQuery() {
        val result = engine.execute(ktql {
            TweetsMetaQuery {
                count()
            }
        })
        val data = result.get()
        val json: String = data.getData<Map<*, *>>().toString()
        assertEquals("{TweetsMeta={count=5}}", json)
    }

    @Test
    fun queryWithInput() {
        val result = engine.execute(ktql {
            TweetQuery("12") {
                id()
            }
        })
        val data = result.get()
        val json: String = data.getData<Map<*, *>>().toString()
        assertEquals("{Tweet={id=12}}", json)
    }

    @Test
    fun queryWithInputAndStarProject() {
        val result = engine.execute(ktql {
            TweetQuery("12") {
                all()
            }
        })
        val data = result.get()
        val json: String = data.getData<Map<*, *>>().toString()
        assertEquals("{Tweet={Author=null, Stats=null, body=This is a tweet, date=null, id=12}}", json)
    }
}