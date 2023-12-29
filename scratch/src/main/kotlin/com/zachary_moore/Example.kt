package com.zachary_moore.ktql

import com.zachary_moore.engine.*

class KTQLImpl : KTQL {
    private val selections = arrayListOf<Field<*, *>>()
    override val selected: List<Field<*, *>>
        get() = selections.toList()

    fun TweetQuery(init: Tweet.() -> Unit) {
        val obj = Tweet()
        obj.init()
        val operation = TweetQuery(obj)
        selections.addAll(operation.selections)
    }

    fun TweetsQuery(init: Tweet.() -> Unit) {
        val obj = Tweet()
        obj.init()
        val operation = TweetsQuery(obj)
        selections.addAll(operation.selections)
    }

    fun TweetsMetaQuery(init: Meta.() -> Unit) {
        val obj = Meta()
        obj.init()
        val operation = TweetsMetaQuery(obj)
        selections.addAll(operation.selections)
    }

    fun UserQuery(init: User.() -> Unit) {
        val obj = User()
        obj.init()
        val operation = UserQuery(obj)
        selections.addAll(operation.selections)
    }

    fun NotificationsQuery(init: Notification.() -> Unit) {
        val obj = Notification()
        obj.init()
        val operation = NotificationsQuery(obj)
        selections.addAll(operation.selections)
    }

    fun NotificationsMetaQuery(init: Meta.() -> Unit) {
        val obj = Meta()
        obj.init()
        val operation = NotificationsMetaQuery(obj)
        selections.addAll(operation.selections)
    }
}

fun ktql(init: KTQLImpl.() -> Unit): KTQL {
    val ktql = KTQLImpl()
    ktql.init()
    return ktql
}

abstract class KTQLOperation<RESULTANT_TYPE: KTQLType<RESULTANT_TYPE>> {
    val selections = arrayListOf<Field<RESULTANT_TYPE, *>>()
}

class TweetQuery(
    obj: Tweet
): KTQLOperation<Tweet>() {
    init {
        selections.addAll(obj.selected)
    }
}
class TweetsQuery(
    obj: Tweet
): KTQLOperation<Tweet>() {
    init {
        selections.addAll(obj.selected)
    }
}
class TweetsMetaQuery(
    obj: Meta
): KTQLOperation<Meta>() {
    init {
        selections.addAll(obj.selected)
    }
}
class UserQuery(
    obj: User
): KTQLOperation<User>() {
    init {
        selections.addAll(obj.selected)
    }
}
class NotificationsQuery(
    obj: Notification
): KTQLOperation<Notification>() {
    init {
        selections.addAll(obj.selected)
    }
}
class NotificationsMetaQuery(
    obj: Meta
): KTQLOperation<Meta>() {
    init {
        selections.addAll(obj.selected)
    }
}

class Tweet : ObjectKTQLType<Tweet> {
    private val selections: ArrayList<Field<Tweet, *>> = arrayListOf()
    override val selected: List<Field<Tweet, *>>
        get() = selections.toList()
    fun id() {
        selections.add(SimpleField("id"))
    }
    fun body() {
        selections.add(SimpleField("body"))
    }
    fun date() {
        selections.add(SimpleField("date"))
    }
    fun Author(init: User.() -> Unit) {
        val obj = User()
        obj.init()
        selections.add(ComplexField("Author", obj))
    }
    fun Stats(init: Stat.() -> Unit) {
        val obj = Stat()
        obj.init()
        selections.add(ComplexField("Stats", obj))
    }
    override fun all() {
        id()
        body()
        date()
        Author {
            all()
        }
        Stats {
            all()
        }
    }
}
class User : ObjectKTQLType<User> {
    private val selections: ArrayList<Field<User, *>> = arrayListOf()
    override val selected: List<Field<User, *>>
        get() = selections.toList()
    fun id() {
        selections.add(SimpleField("id"))
    }
    fun username() {
        selections.add(SimpleField("username"))
    }
    fun first_name() {
        selections.add(SimpleField("first_name"))
    }
    fun last_name() {
        selections.add(SimpleField("last_name"))
    }
    fun full_name() {
        selections.add(SimpleField("full_name"))
    }
    fun name() {
        selections.add(SimpleField("name"))
    }
    fun avatar_url() {
        selections.add(SimpleField("avatar_url"))
    }
    override fun all() {
        id()
        username()
        first_name()
        last_name()
        full_name()
        name()
        avatar_url()
    }
}
class Stat : ObjectKTQLType<Stat> {
    private val selections: ArrayList<Field<Stat, *>> = arrayListOf()
    override val selected: List<Field<Stat, *>>
        get() = selections.toList()
    fun views() {
        selections.add(SimpleField("views"))
    }
    fun likes() {
        selections.add(SimpleField("likes"))
    }
    fun retweets() {
        selections.add(SimpleField("retweets"))
    }
    fun responses() {
        selections.add(SimpleField("responses"))
    }
    override fun all() {
        views()
        likes()
        retweets()
        responses()
    }
}
class Notification : ObjectKTQLType<Notification> {
    private val selections: ArrayList<Field<Notification, *>> = arrayListOf()
    override val selected: List<Field<Notification, *>>
        get() = selections.toList()
    fun id() {
        selections.add(SimpleField("id"))
    }
    fun date() {
        selections.add(SimpleField("date"))
    }
    fun type() {
        selections.add(SimpleField("type"))
    }
    override fun all() {
        id()
        date()
        type()
    }
}
class Meta : ObjectKTQLType<Meta> {
    private val selections: ArrayList<Field<Meta, *>> = arrayListOf()
    override val selected: List<Field<Meta, *>>
        get() = selections.toList()
    fun count() {
        selections.add(SimpleField("count"))
    }
    override fun all() {
        count()
    }
}
class KTQLInt : TerminalKTQLType<KTQLInt>
class KTQLFloat : TerminalKTQLType<KTQLFloat>
class KTQLString : TerminalKTQLType<KTQLString>
class KTQLBoolean : TerminalKTQLType<KTQLBoolean>
class KTQLID : TerminalKTQLType<KTQLID>
class Url : TerminalKTQLType<Url>
class Date : TerminalKTQLType<Date>
