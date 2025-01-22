package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibrosRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findTop5ByOrderByNumeroDescargasDesc();

    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE a.nombre LIKE %:nombreAutor%")
    List<Libro> findLibrosByAutorNombre(String nombreAutor);

    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE i = :idioma")
    List<Libro> findLibrosByIdioma(String idioma);
}
