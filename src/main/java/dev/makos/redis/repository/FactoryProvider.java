package dev.makos.redis.repository;

import dev.makos.redis.domain.City;
import dev.makos.redis.domain.Country;
import dev.makos.redis.domain.CountryLanguage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryProvider {

    public static SessionFactory getSessionFactory() {
        final SessionFactory sessionFactory;

        sessionFactory = new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .buildSessionFactory();
        return sessionFactory;
    }
}
