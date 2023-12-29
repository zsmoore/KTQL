package com.zachary_moore.engine

interface KTQL {
    val selected: List<Field<*, *>>
}

sealed interface KTQLType<TYPE: KTQLType<TYPE>>

interface TerminalKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE>

interface ObjectKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE> {
    fun all()
    val selected: List<Field<TYPE, *>>
}

sealed class Field<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>> {
    abstract val gqlRepresentation: String
}

class SimpleField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String
): Field<PARENT, TYPE>()

class ComplexField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String,
    val innerObject: ObjectKTQLType<TYPE>
): Field<PARENT, TYPE>()