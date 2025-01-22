LiterAlura
![titalura](https://github.com/Danmargiela/LiterAlura/assets/16968289/b8d5890a-f5f6-43fd-aaaf-7a52b0b8b893)

Bienvenidos a LiterAlura, un proyecto desarrollado como parte del desafío de backend de Alura para Oracle Next Education. Este proyecto permite a los usuarios buscar y registrar libros en una base de datos, así como consultar información sobre los libros registrados.

Descripción del Proyecto

LiterAlura es una aplicación de consola desarrollada en Java utilizando Spring Boot, Spring Data JPA y PostgreSQL. La aplicación permite interactuar con la API de Gutendex para buscar libros y almacenarlos en una base de datos local.

Funcionalidades

	1.	Buscar Libro por Título y Guardarlo en la Base de Datos
	•	Permite buscar un libro por su título utilizando la API de Gutendex.
	•	Guarda el libro en la base de datos si no está registrado previamente.
	2.	Listar Libros Registrados
	•	Muestra una lista de todos los libros registrados en la base de datos.
	3.	Listar Autores Registrados
	•	Muestra una lista de todos los autores registrados en la base de datos.
	4.	Listar Autores Vivos en un Determinado Año
	•	Permite ingresar un año y listar los autores que estaban vivos en ese año.
	5.	Listar Libros por Idioma
	•	Permite ingresar un código de idioma (ES, EN, FR, PT) y listar los libros registrados en ese idioma.

Requisitos

	•	Java 17
	•	Maven
	•	PostgreSQL
 
API de Gutendex

Este proyecto utiliza la API de Gutendex para buscar libros. Gutendex es una API basada en el Proyecto Gutenberg, una biblioteca digital con más de 70,000 libros disponibles para descargar gratuitamente. La aplicación LiterAlura se conecta a esta API para obtener información sobre los libros y registrarlos en la base de datos.

Autor

Daniel Sanchez
