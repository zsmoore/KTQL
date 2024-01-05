//import com.zachary_moore.ktql.engine.KTQL;
//import com.zachary_moore.ktql.ExampleKt;
//import com.zachary_moore.ktql.User;
//import com.zachary_moore.ktql.engine.KTQLStringifyKt;
//import kotlin.Unit;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//public class KTQLShouldJava {
//
//    private static KTQL simpleQuery() {
//        return ExampleKt.ktql(ktql -> {
//            ktql.TweetQuery( tweet -> {
//                tweet.id();
//                tweet.Author( author -> {
//                    author.last_name();
//                    simpleUser(author);
//                    return Unit.INSTANCE;
//                });
//                tweet.Stats( stat -> {
//                    stat.all();
//                    return Unit.INSTANCE;
//                });
//                return Unit.INSTANCE;
//            });
//            return Unit.INSTANCE;
//
//        });
//    }
//
//    private static void simpleUser(User user) {
//        user.first_name();
//    }
//
//    @Test
//    public void stringifyKTQLJaava() {
//        KTQL res = simpleQuery();
//        String result = KTQLStringifyKt.stringify(res);
//        Assertions.assertEquals("Author{last_name, first_name}, id, Stats{responses, retweets, views, likes}", result);
//    }
//}
