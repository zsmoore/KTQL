package com.zachary_moore.util

import graphql.language.ListType
import graphql.language.NonNullType
import graphql.language.Type
import graphql.language.TypeName

fun getBaseTypeName(type: Type<*>): String {
    return unwrapType(type).name
}

fun unwrapType(type: Type<*>): TypeName {
    var toUnwrap = type
    while (true) {
        if (!isWrapped(toUnwrap)) {
            require(toUnwrap is TypeName) {
                "Unwrapped type but did not get type name"
            }
            return toUnwrap
        }
        toUnwrap = unwrap(toUnwrap)
    }
}

private fun isWrapped(type: Type<*>): Boolean {
    return isList(type) || isNonNull(type)
}

private fun isList(type: Type<*>): Boolean {
    return type is ListType
}

fun isNonNull(type: Type<*>): Boolean {
    return type is NonNullType
}

fun isWrappedListOrList(type: Type<*>): Boolean {
    return type is ListType ||
            (type is NonNullType && type.type is ListType)
}

private fun unwrap(type: Type<*>): Type<*> {
    return when (type) {
        is NonNullType -> type.type
        is ListType -> type.type
        else -> type
    }
}

fun isPrimitive(typeName: String): Boolean {
    return when(typeName) {
        "String" -> true
        "Boolean" -> true
        "Int" -> true
        "Float" -> true
        "ID" -> true
        else -> false
    }
}

fun isPrimitive(typeName: TypeName): Boolean {
    return isPrimitive(typeName.name)
}