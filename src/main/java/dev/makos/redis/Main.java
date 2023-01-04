package dev.makos.redis;

import dev.makos.redis.domain.City;
import dev.makos.redis.mapper.CityCountryMapper;
import dev.makos.redis.redis.CityCountry;
import dev.makos.redis.redis.RedisRunner;
import dev.makos.redis.repository.MainRepository;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

import java.util.List;

import static java.util.Objects.nonNull;

public class Main {

    private final SessionFactory sessionFactory;
    private final MainRepository mainRepository;
    private final RedisRunner redisRunner;

    private final CityCountryMapper cityCountryMapper;

    public Main() {
        sessionFactory = FactoryProvider.getSessionFactory();
        mainRepository = new MainRepository(sessionFactory);
        redisRunner = new RedisRunner();
        cityCountryMapper = new CityCountryMapper();
    }

    public static void main(String[] args) {
        Main main = new Main();
        List<City> allCities = main.mainRepository.fetchData();
        List<CityCountry> preparedData = main.cityCountryMapper.transformData(allCities);
        main.redisRunner.pushToRedis(preparedData);

        main.sessionFactory.getCurrentSession().close();

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        main.redisRunner.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        main.mainRepository.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        main.shutdown();

    }

    private void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisRunner.getRedisClient())) {
            redisRunner.getRedisClient().shutdown();
        }
    }
}