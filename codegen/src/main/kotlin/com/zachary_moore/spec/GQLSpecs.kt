package com.zachary_moore.spec

data class Schema(
    val queries: List<Query>,
    val mutations: List<Mutation>,
    val types: List<Type>
)

data class Query(
    val name: String,
    val resultantType: Lazy<Type>,
    val inputs: List<InputType>,
)

data class Mutation(
    val name: String,
    val resultantType: Lazy<Type>,
    val inputs: List<InputType>,
)

sealed interface Field {
    val fieldName: String
    val isPrimitive: Boolean
}

data class Type(
    val name: String,
    val fields: List<Field>,
)

data class InputType(
    val variableName: String,
    val typeName: String,
    val isList: Boolean = false,
    val isNonNull: Boolean = false,
)

data class ComplexField(
    override val fieldName: String,
    val fieldType: Lazy<Type>,
    override val isPrimitive: Boolean = false,
): Field

data class SimpleField(
    override val fieldName: String,
    val fieldType: Lazy<Type>,
    override val isPrimitive: Boolean = true,
): Field

