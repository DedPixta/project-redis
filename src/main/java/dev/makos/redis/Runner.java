package dev.makos.redis;

import dev.makos.redis.domain.City;
import dev.makos.redis.mapper.CityCountryMapper;
import dev.makos.redis.repository.FactoryProvider;
import dev.makos.redis.repository.redis.CityCountry;
import dev.makos.redis.repository.redis.RedisRunner;
import dev.makos.redis.repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Slf4j
public class Runner {

    // Change this to test different number of queries
    public static final int NUMBER_OF_QUERIES = 400;

    public static void main(String[] args) {
        // Init resources
        SessionFactory sessionFactory = FactoryProvider.getSessionFactory();
        Repository repository = new Repository(sessionFactory);
        RedisRunner redisRunner = new RedisRunner();
        CityCountryMapper cityCountryMapper = new CityCountryMapper();
        Random random = new Random();

        // Prepare data
        List<City> allCities = repository.fetchData();
        List<CityCountry> preparedData = cityCountryMapper.transformData(allCities);
        redisRunner.pushToRedis(preparedData);
        sessionFactory.getCurrentSession().close();

        // Test data
        List<Integer> ids = IntStream.range(0, NUMBER_OF_QUERIES)
                .map(i -> random.nextInt(4000) + 1)
                .boxed()
                .toList();

        long startRedis = System.currentTimeMillis();
        redisRunner.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        repository.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        log.info("Redis: {} ms", (stopRedis - startRedis));
        log.info("MySQL: {} ms", (stopMysql - startMysql));

        // Close resources
        sessionFactory.close();
        if (nonNull(redisRunner.getRedisClient())) {
            redisRunner.getRedisClient().shutdown();
        }
    }
}
