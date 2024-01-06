import com.zachary_moore.ktql.engine.KTQL;
import com.zachary_moore.ktql.ExampleKt;
import com.zachary_moore.ktql.User;
import com.zachary_moore.ktql.engine.KTQLStringifyKt;
import kotlin.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KTQLShouldJava {

    private static KTQL simpleQuery() {
        return ExampleKt.ktql(ktql -> {
            ktql.TweetQuery( tweet -> {
                tweet.id();
                tweet.Author( author -> {
                    author.last_name();
                    simpleUser(author);
                    return Unit.INSTANCE;
                });
                tweet.Stats( stat -> {
                    stat.all();
                    return Unit.INSTANCE;
                });
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;

        });
    }

    private static void simpleUser(User user) {
        user.first_name();
    }

    @Test
    public void stringifyKTQLJava() {
        KTQL res = simpleQuery();
        String result = KTQLStringifyKt.stringifySorted(res);
        Assertions.assertEquals("Author{first_name, last_name}, Stats{likes, responses, retweets, views}, id", result);
    }
}
