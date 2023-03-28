package dev.makos.redis.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RedisRunner {
    public static final int PORT = 6379;
    public static final String URI = "localhost";

    private final ObjectMapper mapper;
    private final RedisClient redisClient;

    public RedisRunner() {
        this.mapper = new ObjectMapper();
        this.redisClient = prepareRedisClient();
    }

    @SuppressWarnings("unused")
    public RedisClient prepareRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create(URI, PORT));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            log.info("Connected to Redis");
        }
        return redisClient;
    }

    public void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}
