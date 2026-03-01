package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @OneToMany(mappedBy = "autor")
    private List<Libro> libros;

    public Autor() {}

    public Autor(String nombre, Integer nacimiento, Integer fallecimiento) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    public String getNombre() { return nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public Integer getFallecimiento() { return fallecimiento; }

    @Override
    public String toString() {
        return """
                Autor: %s
                Nacimiento: %s
                Fallecimiento: %s
                -------------------------
                """.formatted(
                nombre,
                nacimiento != null ? nacimiento : "Desconocido",
                fallecimiento != null ? fallecimiento : "Vivo"
        );
    }
}