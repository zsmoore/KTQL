import com.zachary_moore.JavaQuery
import com.zachary_moore.engine.stringify
import com.zachary_moore.simpleQuery
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KTQLShould {

    @Test
    fun simple() {
        val res = simpleQuery()
        assertEquals(res.selected.size, 1)
    }

    @Test
    fun stringifyKTQL() {
        val res = simpleQuery()
        val str = stringify(res)
        assertEquals("id, Author{last_name, first_name}, Stats{views, likes, retweets, responses}", str)
    }

    @Test
    fun stringifyKTQLJava() {
        val res = JavaQuery.simpleQuery()
        val str = stringify(res)
        assertEquals("id, Author{last_name, first_name}, Stats{views, likes, retweets, responses}", str)
    }
}