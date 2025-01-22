package com.alura.literalura;

import com.alura.literalura.model.Datos;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.repository.LibrosRepository;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    @Autowired
    private LibrosRepository librosRepository;

    @Autowired
    private AutorRepository autorRepository;

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    @Override
    public void run(String... args) throws Exception {
        muestraMenu();
    }

    public void muestraMenu() {
        int opcion = -1;
        while (opcion != 0) {
            try {
                var menu = """
                        -----------------------------------
                        Elije la opción a través de su número:
                        1- Buscar libro por titulo 
                        2- Listar libros registrados
                        3- Listar autores registrados
                        4- listar autores vivos en un determinado año
                        5- listar libros por idioma
                        0 - Salir
                        -----------------------------------
                        """;
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                teclado.nextLine();
            }
        }
    }

    private void buscarLibroPorTitulo() {
        while (true) {
            System.out.println("Ingrese el nombre del libro que desea buscar (o 0 para regresar al menú principal):");
            var tituloLibro = teclado.nextLine();

            if (tituloLibro.equals("0")) {
                break;
            }

            if (librosRepository.findByTituloContainsIgnoreCase(tituloLibro).isPresent()) {
                System.out.println("El libro ya está registrado en la base de datos.");
                continue;
            }

            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));


            var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
            if (datosBusqueda.resultados().isEmpty()) {
                System.out.println("Libro no encontrado.");
                continue;
            }

            System.out.println("-----------------------------------");
            System.out.println("Libros encontrados:");
            for (int i = 0; i < Math.min(10, datosBusqueda.resultados().size()); i++) {
                System.out.println((i + 1) + ". " + datosBusqueda.resultados().get(i).titulo());
            }
            System.out.println("Seleccione el número del libro que desea guardar (o 0 para cancelar):");
            int seleccion = teclado.nextInt();
            teclado.nextLine();

            if (seleccion == 0) {
                continue;
            }

            if (seleccion > 0 && seleccion <= datosBusqueda.resultados().size()) {
                DatosLibro libroSeleccionado = datosBusqueda.resultados().get(seleccion - 1);
                Libro libro = new Libro(libroSeleccionado);

                if (libro.getIdiomas() == null || libro.getIdiomas().isEmpty()) {
                    System.out.println("No se encontraron idiomas para el libro.");
                } else {
                    imprimirDetallesLibro(libro);
                    librosRepository.save(libro);
                    System.out.println("Libro guardado en la base de datos.");
                }
            } else {
                System.out.println("Selección inválida.");
            }
        }
    }

    private void imprimirDetallesLibro(Libro libro) {
        System.out.println("------------LIBRO-----------------");
        System.out.println("Titulo: " + libro.getTitulo());
        libro.getAutores().forEach(autor -> System.out.println("Autor: " + autor.getNombre()));
        System.out.println("Idioma: " + String.join(", ", libro.getIdiomas()));
        System.out.println("Numero de descargas: " + libro.getNumeroDescargas());
        System.out.println("----------------------------------");
    }

    private void listarLibrosRegistrados() {
        System.out.println("Libros registrados:");
        librosRepository.findAll().forEach(libro -> imprimirDetallesLibro(libro));
    }

    private void listarAutoresRegistrados() {
        System.out.println("Autores registrados:");
        autorRepository.findAll().stream()
                .distinct()
                .forEach(autor -> System.out.println("Autor: " + autor.getNombre()));
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para listar los autores vivos:");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();

            var autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                autoresVivos.forEach(autor -> System.out.println("Autor: " + autor));
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para listar los libros (ES, EN, FR, PT):");
        var idioma = teclado.nextLine().toLowerCase();

        var librosPorIdioma = librosRepository.findLibrosByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + idioma);
        } else {
            librosPorIdioma.forEach(libro -> imprimirDetallesLibro(libro));
        }
    }
}