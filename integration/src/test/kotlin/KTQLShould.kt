
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

    private fun User.simpleUser() {
        first_name()
    }

    @Test
    fun simple() {
        val res = simpleQuery()
        assertEquals(res.selected.size, 1)
    }

    @Test
    fun stringifyKTQL() {
        val res = simpleQuery()
        val str = stringifySorted(res)
        assertEquals("Tweets(limit: Int, skip: Int, sort_field: String, sort_order: String) { Author{first_name, last_name}, Stats{likes, responses, retweets, views}, id}", str)
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
        assertEquals("Tweets(limit: Int, skip: Int, sort_field: String, sort_order: String) { id}", str)
    }
}