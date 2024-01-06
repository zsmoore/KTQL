package com.zachary_moore

import com.zachary_moore.ktql.engine.KTQL
import com.zachary_moore.ktql.engine.stringify
import com.zachary_moore.ktql.ktql

fun main() {
    print(stringify(query()))
}

fun query(): KTQL {
    return ktql {
        TweetsQuery {
            id()
        }
    }
}