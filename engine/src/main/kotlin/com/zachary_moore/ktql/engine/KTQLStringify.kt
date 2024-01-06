package com.zachary_moore.ktql.engine

fun stringify(ktql: KTQL): String {
    return ktql.selected.joinToString { stringify(it) }
}

fun stringifySorted(ktql: KTQL): String {
    return ktql.selected.toSortedSet(fieldComparator()).joinToString { stringify(it, sorted = true) }
}

fun fieldComparator(): Comparator<Field<*, *>> {
    return Comparator { o1, o2 -> o1.gqlRepresentation.compareTo(o2.gqlRepresentation) }
}

private fun stringify(field: Field<*, *>, sorted: Boolean = false): String {
    return when (field) {
        is SimpleField -> field.gqlRepresentation
        is ComplexField -> field.gqlRepresentation + "{" +
                if (sorted) {
                    field.innerObject.selected.toSortedSet(fieldComparator()).joinToString {
                        stringify(it, sorted = sorted)
                    } + "}"
                } else {
                    field.innerObject.selected.joinToString {
                        stringify(it, sorted = sorted)
                    } + "}"
                }
    }
}