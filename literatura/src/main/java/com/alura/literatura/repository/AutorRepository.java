package com.alura.literatura.repository;

import com.alura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreContainingIgnoreCase(String name);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :year AND (a.fechaDeFallecimiento > :year OR a.fechaDeFallecimiento IS NULL)")
    List<Autor> findAuthorsAliveInYear(int year);
}