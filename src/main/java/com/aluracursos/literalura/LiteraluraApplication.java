package com.aluracursos.literalura;

import com.aluracursos.literalura.api.ConsumoAPI;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.repository.AutorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final LibroRepository repository;

    private final AutorRepository autorRepository;

    public LiteraluraApplication(LibroRepository repository,
                                 AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {

            System.out.println("""
        ===== LITERALURA =====
        1 - Buscar libro por título
        2 - Listar todos los libros
        3 - Listar libros por idioma
        4 - Listar autores
        5 - Listar autores vivos en un año
        6 - Cantidad de libros por idioma
        0 - Salir
        """);

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibro(scanner);
                        break;

                    case 0:
                        System.out.println("Cerrando aplicación...");
                        break;

                    case 2:
                        repository.findAll().forEach(System.out::println);
                        break;

                    case 3:
                        System.out.println("Ingrese el idioma (ej: en, es, fr):");
                        String idioma = scanner.nextLine();
                        repository.findByIdioma(idioma).forEach(System.out::println);
                        break;

                    case 4:
                        autorRepository.findAll().forEach(System.out::println);
                        break;

                    case 5:
                        System.out.println("Ingrese el año:");
                        try {
                            Integer anio = Integer.parseInt(scanner.nextLine());

                            autorRepository
                                    .findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqualOrNacimientoLessThanEqualAndFallecimientoIsNull(
                                            anio, anio, anio
                                    )
                                    .forEach(System.out::println);

                        } catch (NumberFormatException e) {
                            System.out.println("Debe ingresar un número válido.");
                        }
                        break;

                    case 6:
                        System.out.println("Ingrese idioma (ej: en, es):");
                        String idiomaConsulta = scanner.nextLine();
                        long cantidad = repository.countByIdioma(idiomaConsulta);
                        System.out.println("Cantidad de libros en " + idiomaConsulta + ": " + cantidad);
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private void buscarLibro(Scanner scanner) {

        System.out.println("Ingrese el nombre del libro que desea buscar:");
        String titulo = scanner.nextLine();

        String url = "https://gutendex.com/books/?search=" +
                URLEncoder.encode(titulo, StandardCharsets.UTF_8);

        try {
            ConsumoAPI consumo = new ConsumoAPI();
            String json = consumo.obtenerDatos(url);

            ObjectMapper mapper = new ObjectMapper();
            Datos datos = mapper.readValue(json, Datos.class);

            if (datos.getResults().isEmpty()) {
                System.out.println("No se encontraron resultados.");
                return;
            }

            DatosLibro datosLibro = datos.getResults().get(0);

// Tomamos el primer autor
            var datosAutor = datosLibro.getAuthors().get(0);

// Creamos entidad Autor
            Autor autor = new Autor(
                    datosAutor.getName(),
                    datosAutor.getBirthYear(),
                    datosAutor.getDeathYear()
            );

// Guardamos el autor
            autorRepository.save(autor);

// Ahora creamos el libro con el objeto Autor
            Libro libro = new Libro(
                    datosLibro.getTitle(),
                    datosLibro.getLanguages().isEmpty() ? "Desconocido"
                            : datosLibro.getLanguages().get(0),
                    datosLibro.getDownloadCount(),
                    autor
            );

// Guardamos el libro
            repository.save(libro);

            System.out.println("Libro y autor guardados correctamente.");


            repository.save(libro);

            System.out.println("Libro guardado exitosamente:");
            System.out.println(libro);

        } catch (Exception e) {
            System.out.println("Error al buscar el libro.");
        }
    }
}