package com.zachary_moore.engine

sealed interface KTQLType<TYPE: KTQLType<TYPE>>

interface TerminalKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE>

interface ObjectKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE> {
    val allFields: List<Field<TYPE, *>>
}
//
//class ID private constructor(): TerminalKTQLType<ID>
//class KTQLString: TerminalKTQLType<KTQLString>
//class KTQLInt: TerminalKTQLType<KTQLInt>
//class Date: TerminalKTQLType<Date>
//class Url: TerminalKTQLType<Url>
//
//class User private constructor(): ObjectKTQLType<User> {
//    companion object {
//        val id: SimpleField<User, ID> = SimpleField("id")
//        val username: SimpleField<User, KTQLString> = SimpleField("username")
//        val first_name: SimpleField<User, KTQLString> = SimpleField("first_name")
//        val last_name: SimpleField<User, KTQLString> = SimpleField("last_name")
//        val full_name: SimpleField<User, KTQLString> = SimpleField("full_name")
//        val name: SimpleField<User, KTQLString> = SimpleField("name")
//        val avatar_url: SimpleField<User, Url> = SimpleField("avatar_url")
//        val allFields: List<Field<User, *>> =
//            listOf(
//                id,
//                username,
//                first_name,
//                last_name,
//                full_name,
//                name,
//                avatar_url
//            )
//    }
//    override val allFields: List<Field<User, *>> = User.allFields
//}
//
//class Stat private constructor(): ObjectKTQLType<Stat> {
//    companion object {
//        val views: SimpleField<Stat, KTQLInt> = SimpleField("views")
//        val likes: SimpleField<Stat, KTQLInt> = SimpleField("likes")
//        val retweets: SimpleField<Stat, KTQLInt> = SimpleField("retweets")
//        val responses: SimpleField<Stat, KTQLInt> = SimpleField("responses")
//        val allFields: List<Field<Stat, *>> =
//            listOf(
//                views,
//                likes,
//                retweets,
//                responses
//            )
//    }
//    override val allFields: List<Field<Stat, *>>
//        get() = Stat.allFields
//}
//
//class Tweet private constructor(): ObjectKTQLType<Tweet> {
//    companion object {
//        val id: SimpleField<Tweet, ID> = SimpleField("id")
//        val body: SimpleField<Tweet, KTQLString> = SimpleField("body")
//        val date: SimpleField<Tweet, Date> = SimpleField("date")
//        fun author(innerSelection: () -> List<Field<User, *>>): ComplexField<Tweet, User> {
//            return ComplexField("", innerSelection.invoke())
//        }
//        fun stats(innerSelection: () -> List<Field<Stat, *>>): ComplexField<Tweet, Stat> {
//            return ComplexField("", innerSelection.invoke())
//        }
//        val allFields =
//            listOf(
//                id,
//                body,
//                date,
//                author { User.allFields },
//                stats { Stat.allFields },
//            )
//    }
//    override val allFields: List<Field<Tweet, *>>
//        get() = Tweet.allFields
//}
//
//class TweetQuery(
//    private val innerSelection: () -> List<Field<Tweet, *>>
//) : KTQLOperation<Tweet> {
//    override val selection: List<Field<Tweet, *>>
//        get() = innerSelection.invoke()
//}
//
//fun getTweets(): KTQL<Tweet> {
//    return ktql {
//        select {
//            TweetQuery {
//
//            }
//        }
//        TweetQuery {
//            listOf(
//                Tweet.body,
//                Tweet.author {
//                    simpleAuthor()
//                },
//                Tweet.stats { Stat.allFields }
//            )
//        }
//    }
//}
//
//fun simpleAuthor(): List<Field<User, *>> {
//    return listOf(
//        User.name,
//        User.full_name
//    )
//}
