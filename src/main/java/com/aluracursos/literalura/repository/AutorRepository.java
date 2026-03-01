package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Para listar autores vivos en determinado año
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqualOrNacimientoLessThanEqualAndFallecimientoIsNull(
            Integer anio1, Integer anio2, Integer anio3
    );
}
