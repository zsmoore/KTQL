package com.zachary_moore.engine

sealed class Field<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>> {
    abstract val gqlRepresentation: String
}

class SimpleField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String
): Field<PARENT, TYPE>()

class ComplexField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String,
    val fields: List<Field<TYPE, *>>
): Field<PARENT, TYPE>()