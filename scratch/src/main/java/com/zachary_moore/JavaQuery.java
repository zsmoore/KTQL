package com.zachary_moore;

import com.zachary_moore.engine.KTQL;
import com.zachary_moore.ktql.ExampleKt;
import com.zachary_moore.ktql.User;
import kotlin.Unit;

public class JavaQuery {

    public static KTQL simpleQuery() {
        return ExampleKt.ktql( ktql -> {
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
}
