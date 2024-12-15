package com.alura.literatura.service;

import com.alura.literatura.model.Autor;
import com.alura.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    AutorRepository repository;

    public Autor getAuthorByName(String nombre){
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public void saveAuthor(Autor newAuthor){
        repository.save(newAuthor);
    }

    public List<Autor> getAllAuthors() { return repository.findAll();}

    public List<Autor> getAuthorAliveInYear(int year) {
        return repository.findAuthorsAliveInYear(year);
    }
}
