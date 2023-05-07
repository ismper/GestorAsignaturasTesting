package org.example.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final Logger logger = Logger.getLogger(HibernateUtil.class);

    private HibernateUtil(){} // constructor vacio para evitar que java cree uno por defecto, queremos que se utilicen los metodos staticos, no es necesario instanciar nada.

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Exception exception) {
            logger.error("Initial SessionFactory creation failed." + exception);
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
