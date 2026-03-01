package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    public Autor getAutor() {
        return autor;
    }

    public Libro() {}

    public Libro(String titulo, String idioma, Integer numeroDescargas, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
        this.autor = autor;
    }

    public String getTitulo() { return titulo; }
    public String getIdioma() { return idioma; }
    public Integer getNumeroDescargas() { return numeroDescargas; }

    @Override
    public String toString() {
        return """
            Título: %s
            Autor: %s
            Idioma: %s
            Descargas: %d
            -------------------------
            """.formatted(
                titulo,
                autor != null ? autor.getNombre() : "Desconocido",
                idioma,
                numeroDescargas
        );
    }
}
