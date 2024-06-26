
import com.zachary_moore.ktql.SchemaProcessor
import com.zachary_moore.ktql.spec.ComplexField
import com.zachary_moore.ktql.spec.Field
import com.zachary_moore.ktql.spec.SimpleField
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
        assertEquals(schema.operations.size, 9)
        assertEquals(schema.operations[0].name, "Tweet")

        val tweet = schema.operations[0].resultantType.value
        assertEquals(tweet.fields.size, 5)

        val inputType = schema.operations[0].inputs[0].ktTypeName
        assertEquals("String", inputType)

        val inputVariableName = schema.operations[0].inputs[0].variableName
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
}