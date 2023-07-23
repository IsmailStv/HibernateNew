package jdbc.dao;

import jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

public class UserDaoHibernateImpl implements UserDao {
    private static final Util utilInstance = Util.getInstance();
    private static final SessionFactory sessionFactory = utilInstance.getSessionFactory();
    private Transaction tx = null;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS " + utilInstance.getTableName("User") +
                            "(id bigint not null auto_increment, age tinyint, last_name varchar(255), name varchar(255), " +
                            "primary key (id))")
                    .executeUpdate();
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != tx) {
                tx.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS " + utilInstance.getTableName("User"))
                    .executeUpdate();
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != tx) {
                tx.rollback();
            }
        }
    }
}
