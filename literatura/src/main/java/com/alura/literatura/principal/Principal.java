package com.alura.literatura.principal;

import com.alura.literatura.model.*;
//import com.alura.literatura.repository.LibroRepository;

import com.alura.literatura.service.AutorService;
import com.alura.literatura.service.BookService;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    public static final String URL_BASE="https://gutendex.com/books/";
    private ConsumoAPI consumoApi= new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    @Autowired
    private BookService bookService;
    @Autowired
    private AutorService autorService;


    public Principal(BookService bookService, AutorService autorService) {
        this.bookService = bookService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Escriba la opcion a traves de su numero
                    
                    1 - Buscar libros por titulo 
                    2 - Listar libros registrados
                    3 - Listar Autores registrados
                    4 - Listar Autores vivos en un determinado año
                    5 - Listar Libros por idioma
                    0 - Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibros();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnTiempoIndicado();
                case 5:
                    mostrarPorIdioma();
                case 0:
                    break;
            }

        }

    }

    private void mostrarPorIdioma() {
        System.out.println("Ingrese un idioma para buscar.");
        System.out.println("es- Español.\n" +
                           "en- Ingles. \n" +
                           "fr- Frances. \n"+
                           "pt- Portugues.");
        var leanguage = teclado.nextLine();
        switch (leanguage){
            case "es":
                libroPorIdioma("es");
                break;
            case "en":
                libroPorIdioma("en");
                break;
            case "fr":
                libroPorIdioma("fr");
                break;
            case "pt":
                libroPorIdioma("pt");
                break;

        }

    }

    private void libroPorIdioma(String idioma) {
        List<Libro> book = bookService.getBookByIdiomas(idioma);
        if (book.isEmpty()){
            System.out.printf("No hay libros registrados en ese idioma solicitado %s.%n", idioma);
        }else {
            book.forEach(bookInfo ->{
                System.out.println();
                System.out.println("------ LIBRO ------");
                System.out.println("Titulo: "+ bookInfo.getTitulo());
                System.out.println("Autor: "+ (bookInfo.getAutores() != null ? bookInfo.getAutores().getNombre() : "Desconocido"));
                System.out.println("Idioma: "+ bookInfo.getIdioma());
                System.out.println("Numero de descargas: "+ bookInfo.getNumeroDescargas()+"\n");
            });
        }

    }

    private void mostrarAutoresVivosEnTiempoIndicado() {
        boolean flag = true;
        System.out.println("Ingrese un año para buscar.");
        var stringYear = teclado.nextLine();
        int year =0;
        while (flag){
            try{
                year= Integer.parseInt(stringYear);
                flag = false;
            }catch (NumberFormatException n){
                System.out.println("Ingrese un año para buscar.");
                stringYear = teclado.nextLine();
            }
        }

        List<Autor> authors = autorService.getAuthorAliveInYear(year);
        authors.forEach(System.out::println);

    }

    private void mostrarAutoresRegistrados() {
        List<Autor> author = autorService.getAllAuthors();
        author.forEach(autor ->{
            System.out.println();
            System.out.println("------ AUTOR ------");
            System.out.println("Autor: "+autor.getNombre());
            System.out.println("Fecha de nacimiento: "+autor.getFechaDeNacimiento());
            System.out.println("Fecha de muerte: " +autor.getFechaDeFallecimiento()+"\n");
        });

        author.forEach(System.out::println);
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> books = bookService.getAllBooks();
        books.forEach(book ->{
                    System.out.println();
                    System.out.println("------ LIBRO ------");
                    System.out.println("Titulo: "+ book.getTitulo());
                    System.out.println("Autor: "+ (book.getAutores() != null ? book.getAutores().getNombre() : "Desconocido"));
                    System.out.println("Idioma: "+ book.getIdioma());
                    System.out.println("Numero de descargas: "+ book.getNumeroDescargas()+"\n");
                });
    }

    private void buscarLibros() {
        DatosLibros recordBook = getDatosLibro();

        if(recordBook != null){
            Optional<Libro> dbBook = bookService.getBookByTitulo(recordBook.titulo());

            if (dbBook.isPresent()){
                System.out.printf("El libro %s ya existe in la base de datos. ", recordBook.titulo());
            }
            else {
                if(recordBook.autores() != null){
                    Autor author = autorService.getAuthorByName(recordBook.autores().get(0).nombre());
                    if(author == null){
                        author = new Autor(recordBook.autores().get(0));
                    }

                    autorService.saveAuthor(author);
                    Libro book = new Libro(recordBook);
                    book.setAutores(author);
                    bookService.saveBook(book);

                    System.out.println(book);

                }
                else{
                    System.out.println("Faltan datos de autor");
                }
            }
        }
    }

    private DatosLibros getDatosLibro() {
        System.out.println("Escribe el nombre de el libro que deseas buscar");
        var tituloLibro = teclado.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(URL_BASE +"?search=" +tituloLibro.replace(" ", "+"));

        Datos datos = conversor.obtenerDatos(json, Datos.class);


        Optional<DatosLibros> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase())).findFirst();


        if (libroBuscado.isPresent()){
            System.out.println("Libro Encontrado\n");
            System.out.println("----- Libro -----");
            System.out.println("Titulo: "+libroBuscado.get().titulo()+
                    "\n"+"Autor: "+ libroBuscado.get().autores().get(0).nombre()+
                    "\n"+"Idioma: "+ libroBuscado.get().idiomas().get(0)+
                    "\n"+"Numero de descargar: "+ libroBuscado.get().numeroDescargas());
            System.out.println("----------\n");
            return libroBuscado.get();
        }else {
            System.out.println("No se encontró el libro.");
            return null;
        }
    }

}
