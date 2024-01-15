package com.zachary_moore.ktql.engine

import com.apollographql.apollo3.api.Optional
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun stringify(ktql: KTQL): String {
    return stringify(ktql, sorted = false)
}

fun stringifySorted(ktql: KTQL): String {
    return stringify(ktql, sorted = true)
}

private fun stringify(ktql: KTQL, sorted: Boolean): String {
    val operationName = when(ktql.selected.first().type) {
        OperationType.QUERY -> "query"
        OperationType.MUTATION -> "mutation"
        OperationType.SUBSCRIPTION -> "subscription"
    }
    return "$operationName ${ktql.name ?: ""}{" +
            ktql.selected.joinToString { stringify(it, sorted = sorted) } +
            "}"
}

private fun stringify(ktqlOperation: KTQLOperation<*>, sorted: Boolean = false): String {
    return if(ktqlOperation.inputs.filter { (_, value) -> value != null }.isNotEmpty()) {
        val inputMap = if (sorted) ktqlOperation.inputs.toSortedMap() else ktqlOperation.inputs
        "${ktqlOperation.gqlRepresentation}(" +
                inputMap.filter { (_, value) ->
                    value != null
                }.map { (key, value) ->
                    "$key : ${convertValue(value)}"
                }.joinToString(separator = ", ") + ") {" +
                ktqlOperation.selections.toSortedSet(fieldComparator()).joinToString { stringify(it, sorted = sorted) } +
                "}"
    } else {
        "${ktqlOperation.gqlRepresentation} {" +
                ktqlOperation.selections.toSortedSet(fieldComparator()).joinToString { stringify(it, sorted = sorted) } +
                "}"
    }
}

private fun fieldComparator(): Comparator<Field<*, *>> {
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

private fun convertValue(any: Any?): Any {
    return when(any) {
        is String -> any
        is Boolean -> any
        is Int -> any
        is Float -> any
        is List<*> -> convertList(any)
        else -> convertApolloObject(any)
    }
}

private fun convertList(anyList: List<*>): String {
    return "[" + anyList.map { convertValue(it) }.joinToString(", ") + "]"
}

private fun convertApolloObject(any: Any?): String {
    requireNotNull(any) { "Attempting to convert input object but found null" }
    return "{" + any::class.memberProperties.mapNotNull { member ->
        if (member is KProperty1<*, *> && (member as KProperty1<Any, *>).get(any) is Optional<*>) {
            val opt = member.get(any) as Optional<*>
            val actualValue = opt.getOrNull()
            if (actualValue == null) {
                null
            } else {
                member.name to actualValue
            }
        } else {
            null
        }
    }.joinToString(separator = ", ") { memberPair ->
        if (memberPair.second is String) {
            "${memberPair.first} : \"${memberPair.second}\""
        } else if (memberPair.second is List<*>) {
            "${memberPair.first} : ${convertList(memberPair.second as List<*>)}"
        } else if (!isPrimitive(memberPair.second)) {
            "${memberPair.first} : ${convertApolloObject(memberPair.second)}"
        } else {
            "${memberPair.first} : ${memberPair.second}"
        }
    } + "}"
}

private fun isPrimitive(any: Any?): Boolean {
    return when(any) {
        is String -> true
        is Boolean -> true
        is Int -> true
        is Float -> true
        else -> false
    }
}