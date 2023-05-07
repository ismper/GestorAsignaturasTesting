package org.example.dao;

import org.apache.log4j.Logger;
import org.example.Main;
import org.example.exception.ValueNotFoundException;
import org.example.model.Alumno;
import org.example.model.Asignatura;
import org.example.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {
    private static final Logger logger = Logger.getLogger(AlumnoDAO.class);

    private AlumnoDAO(){}

    public static void createAlumno(Alumno alumno) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Creating " + alumno);
            session.save(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
        }
    }

    public static Alumno getAlumnoById(long id) throws ValueNotFoundException {
        Alumno alumno = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            alumno = session.get(Alumno.class, id);
            if (alumno == null) {
                throw new ValueNotFoundException("Alumno", id);
            }
            logger.debug(alumno);
            return alumno;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public static void updateAlumno(Alumno alumno) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Updating " + alumno);
            session.update(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
        }
    }

    public static void deleteAlumno(long id) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Alumno alumno = session.get(Alumno.class, id);
            if (alumno == null) {
                throw new ValueNotFoundException("Alumno" , id);
            }
            logger.debug("Deleting " + alumno);
            session.delete(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
            throw e;
        }
    }

    public static List<Alumno> getAllAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            alumnos = session.createQuery("from Alumno", Alumno.class).list();
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug(alumnos);
        return alumnos;
    }

    public static List<Asignatura> getAsignaturasFromAlumnoById(long id) throws ValueNotFoundException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Alumno alumno = session.get(Alumno.class, id);
            if (alumno == null) {
                throw new ValueNotFoundException("Alumno", id);
            }
            List<Asignatura> asignaturas = alumno.getAsignaturas();
            Hibernate.initialize(asignaturas); // asegura que los asignatruas se hayan cargado de manera diferida
            return asignaturas;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public static void removeAsignaturasFromAlumnoById(long id, List<Asignatura> asignaturas) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Alumno alumno = session.get(Alumno.class, id);
            if(alumno == null) {
                throw new ValueNotFoundException("Alumno", id);
            }
            for (Asignatura asignatura : asignaturas){
                Asignatura asignaturaPersistida = session.get(Asignatura.class, asignatura.getId());
                if(asignaturaPersistida != null) {
                    alumno.getAsignaturas().remove(asignaturaPersistida);
                    asignaturaPersistida.getAlumnos().remove(alumno);
                } else {
                    throw new ValueNotFoundException("Asignatura", id);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
            throw e;
        }
    }

    public static void addAsignaturasFromAlumnoById(long id, List<Asignatura> asignaturas) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Alumno alumno = session.get(Alumno.class, id);
            if(alumno == null) {
                throw new ValueNotFoundException("Alumno", id);
            }
            for (Asignatura asignatura : asignaturas){
                Asignatura asignaturaPersistida = session.get(Asignatura.class, asignatura.getId());
                if(asignaturaPersistida != null) {
                    logger.debug("Adding to " + alumno + " : " + asignaturaPersistida);
                    alumno.getAsignaturas().add(asignaturaPersistida);
                    asignaturaPersistida.getAlumnos().add(alumno);
                } else {
                    throw new ValueNotFoundException("Asignatura", id);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
            throw e;
        }
    }
}
