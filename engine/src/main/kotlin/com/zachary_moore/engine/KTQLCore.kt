package com.zachary_moore.engine

interface KTQL {
    val selected: Set<Field<*, *>>
}

abstract class KTQLOperation<RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> {
    val selections = hashSetOf<Field<RESULTANT_TYPE, *>>()
}

sealed interface KTQLType<TYPE: KTQLType<TYPE>>

interface TerminalKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE>

interface ObjectKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE> {
    fun all()
    val selected: Set<Field<TYPE, *>>
}

sealed class Field<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>> {
    abstract val gqlRepresentation: String
}

data class SimpleField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String
): Field<PARENT, TYPE>()

data class ComplexField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String,
    val innerObject: ObjectKTQLType<TYPE>
): Field<PARENT, TYPE>()