//package com.zachary_moore
//
//import com.apollographql.apollo3.api.Optional
//import com.zachary_moore.integration.type.Obj
//import com.zachary_moore.ktql.engine.KTQL
//import com.zachary_moore.ktql.engine.stringify
//import com.zachary_moore.ktql.ktql
//
//fun main() {
//    print(stringify(query()))
//}
//
//fun query(): KTQL {
//    return ktql {
//        SomeQueryQuery(Obj(Optional.present("abc"))) {
//            id()
//        }
//    }
//}