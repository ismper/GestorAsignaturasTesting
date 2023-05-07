package org.example.dao;

import org.apache.log4j.Logger;
import org.example.exception.ValueNotFoundException;
import org.example.model.Alumno;
import org.example.model.Asignatura;
import org.example.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

public class AsignaturaDAO {
    private static final Logger logger = Logger.getLogger(AsignaturaDAO.class);

    private AsignaturaDAO() {}

    public static void createAsignatura(Asignatura asignatura) throws ConstraintViolationException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Creating " + asignatura);
            session.save(asignatura);
            transaction.commit();
        }catch (ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
        }
    }

    public static Asignatura getAlumnoById(long id) throws ValueNotFoundException {
        Asignatura asignatura = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            asignatura = session.get(Asignatura.class, id);
            if (asignatura == null) {
                throw new ValueNotFoundException("Asignatura", id);
            }
            logger.debug(asignatura);
            return asignatura;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public static void updateAsignatura(Asignatura asignatura) throws ConstraintViolationException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Updating " + asignatura);
            session.update(asignatura);
            transaction.commit();
        }catch (ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
        }
    }

    public static void deleteAsignatura(long id) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Asignatura asignatura = session.get(Asignatura.class, id);
            if (asignatura == null) {
                throw new ValueNotFoundException("Alumno" , id);
            }
            logger.debug("Deleting " + asignatura);
            session.delete(asignatura);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e);
            throw e;
        }
    }

    public static List<Asignatura> getAllAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            asignaturas = session.createQuery("from Asignatura", Asignatura.class).list();
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug(asignaturas);
        return asignaturas;
    }

    public static List<Alumno> getAlumnosFromAsignaturaById(long id) throws ValueNotFoundException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Asignatura asignatura = session.get(Asignatura.class, id);
            if (asignatura == null) {
                throw new ValueNotFoundException("Asignatura", id);
            }
            List<Alumno> alumnos = asignatura.getAlumnos();
            Hibernate.initialize(alumnos); // asegura que los asignatruas se hayan cargado de manera diferida
            return alumnos;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public static void removeAlumnoFromAsignaturaById(long id, List<Alumno> alumnos) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Asignatura asignatura = session.get(Asignatura.class, id);
            if(asignatura == null) {
                throw new ValueNotFoundException("Asignatura", id);
            }
            for (Alumno alumno : alumnos){
                Alumno alumnoPersistido = session.get(Alumno.class, alumno.getId());
                if(alumnoPersistido != null) {
                    asignatura.getAlumnos().remove(alumnoPersistido);
                    alumnoPersistido.getAsignaturas().remove(asignatura);
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

    public static void addAlumnoFromAsignaturaById(long id, List<Alumno> alumnos) throws ValueNotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Asignatura asignatura = session.get(Asignatura.class, id);
            if(asignatura == null) {
                throw new ValueNotFoundException("Asignatura", id);
            }
            for (Alumno alumno : alumnos){
                Alumno alumnoPersistido = session.get(Alumno.class, alumno.getId());
                if(alumnoPersistido != null) {
                    asignatura.getAlumnos().add(alumnoPersistido);
                    alumnoPersistido.getAsignaturas().add(asignatura);
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
