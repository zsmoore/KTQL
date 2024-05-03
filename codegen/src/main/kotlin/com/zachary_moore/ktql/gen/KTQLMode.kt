package com.zachary_moore.ktql.gen

/**
 * Generator Modes for KTQL
 *
 * INLINE_TRANSLATION - DSL will be stringified at runtime and a standard engine
 *      will be used to make GraphQL calls.  Input data will be inlined.
 *
 * FILE_GENERATION - Using KSP, devs write functions which return KTQL.
 *      From these KTQL objects we will generate GraphQL files for use in existing
 *      systems such as apollo based applications
 *
 * CLIENT_BASED - DSL will be converted to graphql files for systems to use query id based systems.
 *      Devs will use the KTQL object directly for making calls.  The generated client will send query id
 *      + variables to server instead of raw queries
 *
 *
 * Based on KTQLMode, the generated KTQL.kt class will have differing APIs with
 * INLINE_TRANSLATION being the most straightforward, requiring variable aliases for
 * any file based generation approach.
 */
enum class KTQLMode {
    INLINE_TRANSLATION,
    FILE_GENERATION,
    CLIENT_BASED
}