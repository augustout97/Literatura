package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gutendex_id;

    @Column(name = "titulo")
    private String titulo;

    private String idioma;

    @ManyToOne
    private Autor autores;

    private Integer numeroDescargas;


    public Libro(DatosLibros datosLibros) {
        this.titulo= datosLibros.titulo();
        this.gutendex_id= datosLibros.gutendex_id();
        this.idioma = String.join(",",datosLibros.idiomas()) ;
        this.numeroDescargas = datosLibros.numeroDescargas();
    }

    public Libro() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutores() {
        return autores;
    }

    public void setAutores(Autor autores) {
        this.autores = autores;
    }

    public List<String> getIdioma() {
        return Arrays.asList(this.idioma.split(","));
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = String.join(",", idioma);
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", gutendex_id=" + gutendex_id +
                ", titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", autores=" + autores +
                ", numeroDescargas=" + numeroDescargas +
                '}';
    }
}
