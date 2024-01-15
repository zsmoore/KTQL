package file

import com.zachary_moore.ktql.engine.stringifySorted
import com.zachary_moore.ktql.file.ktql
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KTQLShould {

    @Test
    fun simpleQueryWithName() {
        val res = ktql("SomeQuery") {
            TweetsMetaQuery {
                count()
            }
        }
        val str = stringifySorted(res)
        assertEquals("query SomeQuery{TweetsMeta {count}}", str)
    }
}