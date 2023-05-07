package org.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asignaturas")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String nombre;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "asignaturas_alumnos",
            joinColumns = {@JoinColumn(name = "asignatura_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "alumno_id", referencedColumnName = "id")}
    )
    private List<Alumno> alumnos = new ArrayList<>();

    public Asignatura() {}

    public Asignatura(String nombre) {
        this.nombre = nombre;
    }

    public Asignatura(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
