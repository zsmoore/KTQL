package com.zachary_moore.ktql.engine

fun stringify(ktql: KTQL): String {
    return ktql.selected.joinToString { stringify(it) }
}

private fun stringify(field: Field<*, *>): String {
    return when (field) {
        is SimpleField -> field.gqlRepresentation
        is ComplexField -> field.gqlRepresentation + "{" + field.innerObject.selected.joinToString {
            stringify(it)
        } + "}"
    }
}