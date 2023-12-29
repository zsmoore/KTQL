package com.zachary_moore.engine
//
//class KTQL<RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> {
//    private val selections = arrayListOf<Field<RESULTANT_TYPE, *>>()
//    fun select(init: KTQLOperation<RESULTANT_TYPE>.() -> Unit) {
//        val operation = KTQLOperation<RESULTANT_TYPE>()
//        operation.init()
//        selections.addAll(operation.selections)
//    }
//}
//
//fun <RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> ktql(init: KTQL<RESULTANT_TYPE>.() -> Unit): KTQL<RESULTANT_TYPE> {
//    val ktql = KTQL<RESULTANT_TYPE>()
//    ktql.init()
//    return ktql
//}
//
//class KTQLOperation<RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> {
//    val selections = arrayListOf<Field<RESULTANT_TYPE, *>>()
//}