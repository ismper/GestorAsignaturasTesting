package org.example;

import org.example.dao.AlumnoDAO;
import org.example.dao.AsignaturaDAO;
import org.example.exception.ValueNotFoundException;
import org.example.model.Alumno;
import org.example.model.Asignatura;
import org.example.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        Alumno johnDoe = new Alumno("John Doe");
        Alumno janeDoe = new Alumno("Jane Doe");
        Asignatura programacion = new Asignatura("Programación");
        ArrayList<Asignatura> asignaturas = new ArrayList<>();
        ArrayList<Alumno> alumnosMatriculadosEnProgramacion = new ArrayList<>();
        ArrayList<Alumno> alumnos = new ArrayList<>();
        asignaturas.add(programacion);
        alumnosMatriculadosEnProgramacion.add(janeDoe);
        alumnos.add(janeDoe);
        alumnos.add(johnDoe);

        AlumnoDAO.createAlumno(johnDoe);
        AlumnoDAO.createAlumno(janeDoe);
        try {
            AsignaturaDAO.createAsignatura(programacion);
            AsignaturaDAO.createAsignatura(programacion);
        } catch (ConstraintViolationException e) {
            logger.error(e.getMessage());
        }

        logger.info(AlumnoDAO.getAllAlumnos());
        logger.info(AsignaturaDAO.getAllAsignaturas());

        try {
            AlumnoDAO.getAlumnoById(3);
        } catch (ValueNotFoundException e) {
            logger.error(e.getMessage());
        }

        logger.info(janeDoe);

        try {
            logger.info(AlumnoDAO.getAsignaturasFromAlumnoById(janeDoe.getId()));
            AlumnoDAO.addAsignaturasFromAlumnoById(janeDoe.getId(), asignaturas);
            logger.info(AlumnoDAO.getAsignaturasFromAlumnoById(janeDoe.getId()));
        } catch (ValueNotFoundException e) {
            logger.error(e);
        }

        try {
            AsignaturaDAO.removeAlumnoFromAsignaturaById(programacion.getId(), alumnosMatriculadosEnProgramacion);
            logger.info(AsignaturaDAO.getAlumnosFromAsignaturaById(programacion.getId()));
            AsignaturaDAO.removeAlumnoFromAsignaturaById(programacion.getId(), alumnos);
        } catch (ValueNotFoundException e) {
            logger.error(e.getMessage());
        }


        HibernateUtil.shutdown();
    }
}