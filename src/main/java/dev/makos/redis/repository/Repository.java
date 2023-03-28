package dev.makos.redis.repository;

import dev.makos.redis.repository.dao.CityDAO;
import dev.makos.redis.domain.City;
import dev.makos.redis.domain.CountryLanguage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Repository {

    private final SessionFactory sessionFactory;
    private final CityDAO cityDAO;

    public Repository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.cityDAO = new CityDAO(sessionFactory);
    }

    public List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    @SuppressWarnings("unused")
    public void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}
