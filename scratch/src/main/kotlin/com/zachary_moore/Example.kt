package com.zachary_moore.ktql

sealed interface KTQLType<TYPE: KTQLType<TYPE>>

interface TerminalKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE>

interface ObjectKTQLType<TYPE: KTQLType<TYPE>>: KTQLType<TYPE> {
    val allFields: List<Field<TYPE, *>>
}

sealed class Field<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>> {
    abstract val gqlRepresentation: String
}

class SimpleField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String
): Field<PARENT, TYPE>()

class ComplexField<PARENT: KTQLType<PARENT>, TYPE: KTQLType<TYPE>>(
    override val gqlRepresentation: String,
    val fields: List<Field<TYPE, *>>
): Field<PARENT, TYPE>()

class KTQL {
    private val selections = arrayListOf<Field<*, *>>()
    val selected: List<Field<*, *>>
        get() = selections.toList()

    fun TweetQuery(init: TweetQuery.() -> Unit) {
        val operation = TweetQuery()
        operation.init()
        selections.addAll(operation.selections)
    }

    fun TweetsQuery(init: TweetsQuery.() -> Unit) {
        val operation = TweetsQuery()
        operation.init()
        selections.addAll(operation.selections)
    }

    fun TweetsMetaQuery(init: TweetsMetaQuery.() -> Unit) {
        val operation = TweetsMetaQuery()
        operation.init()
        selections.addAll(operation.selections)
    }

    fun UserQuery(init: UserQuery.() -> Unit) {
        val operation = UserQuery()
        operation.init()
        selections.addAll(operation.selections)
    }

    fun NotificationsQuery(init: NotificationsQuery.() -> Unit) {
        val operation = NotificationsQuery()
        operation.init()
        selections.addAll(operation.selections)
    }

    fun NotificationsMetaQuery(init: NotificationsMetaQuery.() -> Unit) {
        val operation = NotificationsMetaQuery()
        operation.init()
        selections.addAll(operation.selections)
    }
}

fun ktql(init: KTQL.() -> Unit): KTQL {
    val ktql = KTQL()
    ktql.init()
    return ktql
}

abstract class KTQLOperation<RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> {
    val selections = arrayListOf<Field<RESULTANT_TYPE, *>>()
    fun select(vararg fields: Field<RESULTANT_TYPE, *>) {
        selections.addAll(fields)
    }
}

class TweetQuery: KTQLOperation<Tweet>()
class TweetsQuery: KTQLOperation<Tweet>()
class TweetsMetaQuery: KTQLOperation<Meta>()
class UserQuery: KTQLOperation<User>()
class NotificationsQuery: KTQLOperation<Notification>()
class NotificationsMetaQuery: KTQLOperation<Meta>()

class Tweet private constructor(): ObjectKTQLType<Tweet> {
    private val selections: ArrayList<Field<Tweet, *>> = arrayListOf()
    val selected: List<Field<Tweet, *>>
        get() = selections.toList()
    fun id() {
        selections.add(SimpleField("id"))
    }
    fun Author(init: User.() -> Unit) {
        
    }
    companion object {
        val id : SimpleField<Tweet, KTQLID> = SimpleField("id")
        val body : SimpleField<Tweet, KTQLString> = SimpleField("body")
        val date : SimpleField<Tweet, Date> = SimpleField("date")
        fun Author(innerSelection: () -> List<Field<User, *>>): ComplexField<Tweet, User> {
            return ComplexField("", innerSelection.invoke())
        }
        fun Stats(innerSelection: () -> List<Field<Stat, *>>): ComplexField<Tweet, Stat> {
            return ComplexField("", innerSelection.invoke())
        }
        val allFields =
            listOf(
                id,
                body,
                date,
                Author { User.allFields },
                Stats { Stat.allFields },
            )
    }
    override val allFields: List<Field<Tweet, *>>
        get() = Tweet.allFields
}
class User private constructor(): ObjectKTQLType<User> {
    companion object {
        val id : SimpleField<User, KTQLID> = SimpleField("id")
        val username : SimpleField<User, KTQLString> = SimpleField("username")
        val first_name : SimpleField<User, KTQLString> = SimpleField("first_name")
        val last_name : SimpleField<User, KTQLString> = SimpleField("last_name")
        val full_name : SimpleField<User, KTQLString> = SimpleField("full_name")
        val name : SimpleField<User, KTQLString> = SimpleField("name")
        val avatar_url : SimpleField<User, Url> = SimpleField("avatar_url")
        val allFields =
            listOf(
                id,
                username,
                first_name,
                last_name,
                full_name,
                name,
                avatar_url,
            )
    }
    override val allFields: List<Field<User, *>>
        get() = User.allFields
}
class Stat private constructor(): ObjectKTQLType<Stat> {
    companion object {
        val views : SimpleField<Stat, KTQLInt> = SimpleField("views")
        val likes : SimpleField<Stat, KTQLInt> = SimpleField("likes")
        val retweets : SimpleField<Stat, KTQLInt> = SimpleField("retweets")
        val responses : SimpleField<Stat, KTQLInt> = SimpleField("responses")
        val allFields =
            listOf(
                views,
                likes,
                retweets,
                responses,
            )
    }
    override val allFields: List<Field<Stat, *>>
        get() = Stat.allFields
}
class Notification private constructor(): ObjectKTQLType<Notification> {
    companion object {
        val id : SimpleField<Notification, KTQLID> = SimpleField("id")
        val date : SimpleField<Notification, Date> = SimpleField("date")
        val type : SimpleField<Notification, KTQLString> = SimpleField("type")
        val allFields =
            listOf(
                id,
                date,
                type,
            )
    }
    override val allFields: List<Field<Notification, *>>
        get() = Notification.allFields
}
class Meta private constructor(): ObjectKTQLType<Meta> {
    companion object {
        val count : SimpleField<Meta, KTQLInt> = SimpleField("count")
        val allFields =
            listOf(
                count,
            )
    }
    override val allFields: List<Field<Meta, *>>
        get() = Meta.allFields
}
class KTQLInt : TerminalKTQLType<KTQLInt>
class KTQLFloat : TerminalKTQLType<KTQLFloat>
class KTQLString : TerminalKTQLType<KTQLString>
class KTQLBoolean : TerminalKTQLType<KTQLBoolean>
class KTQLID : TerminalKTQLType<KTQLID>
class Url : TerminalKTQLType<Url>
class Date : TerminalKTQLType<Date>
