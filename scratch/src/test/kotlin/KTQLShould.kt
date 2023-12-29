import com.zachary_moore.simpleQuery
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KTQLShould {

    @Test
    fun simple() {
        val res = simpleQuery()
        assertEquals(res.selected.size, 1)
    }
}