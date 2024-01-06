
import com.zachary_moore.SchemaProcessor
import com.zachary_moore.spec.ComplexField
import com.zachary_moore.spec.Field
import com.zachary_moore.spec.SimpleField
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchemaProcessorShould {

    private lateinit var types: TypeDefinitionRegistry

    @BeforeAll
    fun setup() {
        val rawSchema = this::class.java.getResource("schema.graphql").readText(Charsets.UTF_8)
        types = SchemaParser().parse(rawSchema)
    }

    @Test
    fun parseTestSchema() {
        val schema = SchemaProcessor(types).process()
        assertEquals(schema.queries.size, 6)
        assertEquals(schema.mutations.size, 3)
        assertEquals(schema.queries[0].name, "Tweet")
        assertEquals(schema.mutations[0].name, "createTweet")

        val tweet = schema.queries[0].resultantType.value
        assertEquals(tweet.fields.size, 5)

        val inputType = schema.queries[0].inputs[0].typeName
        assertEquals("GraphQLID", inputType)

        val inputVariableName = schema.queries[0].inputs[0].variableName
        assertEquals("id", inputVariableName)

        val userField = tweet.fields.first { field: Field ->
            field is ComplexField && field.fieldType.value.name == "User"
        }

        val user = (userField as ComplexField).fieldType.value
        assertEquals(user.fields.size, 7)
        assertEquals(user.fields[0].fieldName, "id")
        assertEquals(user.fields[0] is SimpleField, true)
        assertEquals((user.fields[0] as SimpleField).fieldType.value.name, "KTQLID")
    }

//    @Test
//    fun generateCodeGen() {
//        val schema = SchemaProcessor(types).process()
//        Generator(schema).generate()
//    }
}