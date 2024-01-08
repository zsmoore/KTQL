package com.zachary_moore

import com.zachary_moore.spec.*
import com.zachary_moore.util.*
import graphql.language.FieldDefinition
import graphql.language.InputValueDefinition
import graphql.language.ObjectTypeDefinition
import graphql.language.ScalarTypeDefinition
import graphql.schema.idl.TypeDefinitionRegistry

const val QUERY = "Query"
const val MUTATION = "Mutation"

class SchemaProcessor(
    private val typeDefinitions: TypeDefinitionRegistry
) {
    private val typeCache: Map<String, Type> by lazy {
        generateTypeMap()
    }
    private val schema: Schema by lazy {
        processTypes(typeDefinitions)
    }

    fun process(): Schema {
        return schema
    }

    private fun generateTypeMap(): Map<String, Type> {
        val allTypes = typeDefinitions.types() + typeDefinitions.scalars()
        return allTypes.entries.filter { (typeName, type) ->
            typeName != QUERY && typeName != MUTATION &&
                    (type is ObjectTypeDefinition || type is ScalarTypeDefinition)
        }.associate { (typeName, typeDefinition) ->
            when (typeDefinition) {
                is ObjectTypeDefinition -> typeName to processSingleType(typeDefinition)
                is ScalarTypeDefinition -> maybePrefixKTQL(typeName) to processSingleScalar(typeDefinition)
                else -> throw IllegalStateException("Bad state")
            }
        }
    }

    private fun processSingleScalar(scalarTypeDefinition: ScalarTypeDefinition): Type {
        return Type(
            maybePrefixKTQL(scalarTypeDefinition.name),
            listOf()
        )
    }

    private fun maybePrefixKTQL(typeName: String): String {
        var tempTypeName = typeName
        if (isPrimitive(typeName)) {
            tempTypeName = "KTQL$typeName"
        }
        return tempTypeName
    }

    private fun maybeCoerceID(typeName: String): String {
        if (typeName == "ID") {
            return "String"
        }
        return typeName
    }

    private fun processSingleType(objectTypeDefinition: ObjectTypeDefinition): Type {
        return Type(
            objectTypeDefinition.name,
            processFields(objectTypeDefinition.children.filterIsInstance<FieldDefinition>())
        )
    }

    private fun processFields(fieldDefinitions: List<FieldDefinition>): List<Field> {
        return fieldDefinitions.map { fieldDefinition -> processField(fieldDefinition) }
    }

    private fun processField(fieldDefinition: FieldDefinition): Field {
        val type = unwrapType(fieldDefinition.type)
        return if (isPrimitive(type)) {
            SimpleField(
                fieldDefinition.name,
                lazy {
                    requireNotNull(typeCache[maybePrefixKTQL(type.name)]) {
                        "Lazy type reference in simple field was not present"
                    }
                }
            )
        } else if (typeDefinitions.scalars().containsKey(type.name)) {
            SimpleField(
                fieldDefinition.name,
                lazy {
                    requireNotNull(typeCache[maybePrefixKTQL(type.name)]) {
                        "Lazy type reference in simple field was not present"
                    }
                }
            )
        } else {
            ComplexField(
                fieldDefinition.name,
                lazy {
                    requireNotNull(typeCache[type.name]) {
                        "Lazy type reference in complex field was not present"
                    }
                }
            )
        }
    }

    private fun processTypes(typeDefinitions: TypeDefinitionRegistry): Schema {
        val query: ObjectTypeDefinition = typeDefinitions.getType(QUERY).orElseThrow {
            IllegalStateException("Could not find Query object in Schema")
        } as ObjectTypeDefinition
        val mutation: ObjectTypeDefinition = typeDefinitions.getType(MUTATION).orElseThrow {
            IllegalStateException("Could not find Mutation object in Schema")
        } as ObjectTypeDefinition
        return Schema(
            processQueries(query),
            processMutations(mutation),
            typeCache.values.toList()
        )
    }

    private fun processQueries(queryDefinition: ObjectTypeDefinition): List<Query> {
        return queryDefinition.children.map { child ->
            require(child is FieldDefinition)
            processSingleQuery(child)
        }
    }

    private fun processSingleQuery(query: FieldDefinition): Query {
        val returnType = getBaseTypeName(query.type)
        val inputValues = processInputValueDefinitions(query.inputValueDefinitions)
        return Query(
            query.name,
            lazy {
                requireNotNull(typeCache[returnType]) {
                    "Query return type not found in type cache"
                }
            },
            inputValues,
            generateQueryGQLRepresentation(query.name, inputValues)
        )
    }

    private fun processInputValueDefinitions(inputValueDefinitions: List<InputValueDefinition>): List<InputType> {
        return inputValueDefinitions.map { def -> processSingleInputValueDefinition(def) }
    }

    private fun processSingleInputValueDefinition(inputValueDefinition: InputValueDefinition): InputType{
        val isList = isWrappedListOrList(inputValueDefinition.type)
        val baseType = getBaseTypeName(inputValueDefinition.type)
        return InputType(
            inputValueDefinition.name,
            generateGQLTypeName(baseType, isList),
            maybeCoerceID(getBaseTypeName(inputValueDefinition.type)),
            isPrimitive(unwrapType(inputValueDefinition.type)),
            isNonNull(inputValueDefinition.type),
            isList
        )
    }

    private fun processMutations(mutationDefinition: ObjectTypeDefinition): List<Mutation> {
        return mutationDefinition.children.map { child ->
            require(child is FieldDefinition)
            processSingleMutation(child)
        }
    }

    private fun processSingleMutation(mutation: FieldDefinition): Mutation {
        val returnType = getBaseTypeName(mutation.type)
        return Mutation(
            mutation.name,
            lazy {
                requireNotNull(typeCache[returnType]) {
                    "Mutation type not found in type cache"
                }
            },
            processInputValueDefinitions(mutation.inputValueDefinitions)
        )
    }

    private fun generateQueryGQLRepresentation(queryName: String, inputValues: List<InputType>): String {
        if (inputValues.isEmpty()) {
            return queryName
        }
        var gqlRepresentation = "$queryName("
        for (i in inputValues.indices) {
            val inputType = inputValues[i]
            gqlRepresentation += "${inputType.variableName}: ${inputType.gqlTypeName}"
            if (inputType.isNonNull) gqlRepresentation += "!"
            if (i < inputValues.size - 1) gqlRepresentation += ", "
        }
        gqlRepresentation += ")"
        return gqlRepresentation
    }

    private fun generateGQLTypeName(baseType: String, isList: Boolean): String {
        return if (isList) {
            "[$baseType]"
        } else {
            baseType
        }
    }
}
