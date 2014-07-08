package ru.pmsoft.twitterkiller.domain.dataaccess;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.pmsoft.twitterkiller.domain.entity.User;
import ru.pmsoft.twitterkiller.domain.repository.UserRepository;
import ru.pmsoft.twitterkiller.domain.util.HibernateUtil;

import java.util.List;

public class DbUserRepository implements UserRepository {

    @Override
    public void save(User user) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public User getByLogin(String login) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.createCriteria(User.class).add(Restrictions.eq("login", login)).uniqueResult();
        session.close();
        return user;
    }

    @Override
    public Iterable<User> values() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> foundUsers = (List<User>) session.createCriteria(User.class).list();
        session.close();
        return foundUsers;
    }
}