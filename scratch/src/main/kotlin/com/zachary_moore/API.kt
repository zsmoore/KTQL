package com.zachary_moore

import com.zachary_moore.ktql.*

fun simpleQuery(): KTQL {
    return ktql {
        TweetQuery {
            select (
                Tweet.body
            )
        }
        TweetsQuery {
            select(
                Tweet.Author {
                    User.first_name
                }
            )
        }
        TweetsQuery {
            select(
                Tweet.Author {
                    simpleUser()
                }
            )
        }
    }
}

fun simpleUser(): List<Field<User, *>> {
    return listOf(User.full_name, User.last_name)
}
