package com.alura.literatura.service;

import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private LibroRepository repository;

    public Optional<Libro> getBookByTitulo(String titulo){
        return repository.findByTituloIgnoreCase(titulo);

    }

    public void saveBook(Libro newBook){
        repository.save(newBook);
    }


    public List<Libro> getAllBooks() { return repository.findAll();}

    public List<Libro> getBookByIdiomas(String idioma) {
        return repository.findByIdiomaContainingIgnoreCase(idioma);
    }
}
