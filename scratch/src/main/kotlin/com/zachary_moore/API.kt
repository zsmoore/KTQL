package com.zachary_moore

import com.zachary_moore.engine.KTQL
import com.zachary_moore.ktql.User
import com.zachary_moore.ktql.ktql

fun simpleQuery(): KTQL {
    return ktql {
        TweetsQuery {
            id()
            Author {
                last_name()
                simpleUser()
            }
            Stats {
                all()
            }
        }
    }
}

fun User.simpleUser() {
    first_name()
}