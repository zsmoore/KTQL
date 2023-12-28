package com.zachary_moore.com.zachary_moore.spec

data class Document(
    val queries: List<Query>,
    val mutations: List<Mutation>
)

data class Query(
    val name: String
)

data class Mutation(
    val name: String
)

data class Type(
    val name: String,
    val fields: List<Field>
)

data class Field(
    val fieldName: String
)

