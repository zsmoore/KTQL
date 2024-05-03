package com.zachary_moore.ktql.spec

data class Schema(
    val operations: List<Operation>,
    val types: List<Type>
)

data class Operation(
    val name: String,
    val resultantType: Lazy<Type>,
    val inputs: List<InputType>,
    val type: OperationType
)

enum class OperationType {
    QUERY,
    MUTATION,
    SUBSCRIPTION,
}

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
    val gqlTypeName: String,
    val ktTypeName: String,
    val isPrimitive: Boolean,
    val isList: Boolean,
    val isNonNull: Boolean,
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

