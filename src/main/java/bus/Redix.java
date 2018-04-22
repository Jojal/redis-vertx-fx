package bus;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;


import java.util.function.Consumer;

/**
 * Created by jojal on 22/04/2018.
 */
public class Redix {

    static Vertx vertx = Vertx.vertx();

    //Config redis
    static RedisOptions config = new RedisOptions().setHost("127.0.0.1").setPort(6379);

    // Pub
    static RedisClient redisPub = RedisClient.create(vertx, config);

    // Sub
    static RedisClient redisSub = RedisClient.create(vertx, config);


    public static void subscribe(Consumer<JsonObject> consumer) {

        vertx.eventBus().<JsonObject>consumer("io.vertx.redis.drawchannel", received -> {
            JsonObject value = received.body().getJsonObject("value");
            consumer.accept(value);
        });

        redisSub.subscribe("drawchannel", res -> {
            if (res.succeeded()) {
                System.err.println("Subscribe Ok");
            }
        });
    }

    public static void publish(JsonObject obj) {
        redisPub.publish("drawchannel", obj.encode(), res -> {
            if (res.succeeded()) {

            }
        });
    }

    public static void stop() {
        vertx.close();
    }
}
