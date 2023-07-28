package jdbc.dao;

import jdbc.model.User;
import jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    @Override
    public void createUsersTable() {
        Util ut = new Util();
        Transaction tx = null;
        try (Session session = ut.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS users(id bigint auto_increment primary key , name varchar(40), last_name varchar(40), age tinyint );").addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table is created");
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
        }
    }
    @Override
    public void dropUsersTable() {
        Util ut = new Util();
        Transaction tx = null;
        try (Session session = ut.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table is deleted");
        } catch (Exception e) {
            tx.rollback();
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util ut = new Util();
        User user = new User(name, lastName, age);
        Transaction tx = null;
        try (Session session = ut.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User named " + name + " added in table");
        } catch (HibernateException e) {
            System.out.println(e);
        }
    }
    @Override
    public void removeUserById(long id) {
        User user = new User();
        Util ut = new Util();
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createQuery("delete User where id = :userId").setParameter("userId", id).executeUpdate();
            session.getTransaction().commit();
            System.out.println("объект был удален по id");
        } catch (Exception e) {
            System.out.println("Object was not deleted by id");
        }
    }
    @Override
    public List<User> getAllUsers() {
        Util ut = new Util();
        List<User> userList = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            userList = (List<User>) session.createQuery("From " + User.class.getSimpleName()).list();
            session.getTransaction().commit();
            System.out.println("Список объектов получен");
        } catch (Exception e) {
            System.out.println("Список объектов небыл получен");
        }
        return userList;
    }
    @Override
    public void cleanUsersTable() {
        Util ut = new Util();
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table is cleaned");
        } catch (Exception e) {
            System.out.println("Table was not cleaned");
            e.printStackTrace();
        }
    }
}
