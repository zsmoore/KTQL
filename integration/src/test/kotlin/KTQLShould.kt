
import com.apollographql.apollo3.api.Optional
import com.zachary_moore.integration.type.InnerObj
import com.zachary_moore.integration.type.Obj
import com.zachary_moore.ktql.User
import com.zachary_moore.ktql.engine.KTQL
import com.zachary_moore.ktql.engine.stringify
import com.zachary_moore.ktql.engine.stringifySorted
import com.zachary_moore.ktql.ktql
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KTQLShould {

    private fun simpleQuery(): KTQL {
        return ktql {
            TweetsQuery(limit = 10) {
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

    private fun User.simpleUser() {
        first_name()
    }

    @Test
    fun simple() {
        val res = simpleQuery()
        assertEquals(res.selected.size, 1)
    }

    @Test
    fun objectInput() {
        val res = ktql {
            SomeQueryQuery(obj = Obj(Optional.present("abc"))) {
                id()
            }
        }
        val str = stringifySorted(res)
        assertEquals("SomeQuery(obj : {someString : \"abc\"}) {id}", str)
    }

    @Test
    fun innerObject() {
        val res = ktql {
            SomeQueryQuery(obj = Obj(
                Optional.present("abc"),
                Optional.present(InnerObj(Optional.present(10)))
            )) { id() }
        }
        val str = stringifySorted(res)
        assertEquals("SomeQuery(obj : {innerObj : {someInt : 10}, someString : \"abc\"}) {id}", str)
    }

    @Test
    fun stringifyKTQL() {
        val res = simpleQuery()
        val str = stringifySorted(res)
        assertEquals("Tweets(limit : 10) {Author{first_name, last_name}, Stats{likes, responses, retweets, views}, id}", str)
    }

    @Test
    fun avoidDuplicateFieldProjections() {
        val res = ktql {
            TweetsQuery {
                id()
                id()
            }
        }
        val str = stringify(res)
        assertEquals("Tweets {id}", str)
    }
}