# MiniRedSocial

Mini red social con Neo4j y una interfaz grafica Java Swing para demostrar CRUD,
relaciones y consultas complejas de grafos.

## Requisitos

- JDK 21.
- Maven.
- Docker o Neo4j Desktop.

## Arrancar Neo4j

Con Docker:

```bash
docker compose up -d
```

Neo4j Browser queda disponible en:

```text
http://localhost:7474
```

Credenciales de demo:

```text
usuario: neo4j
password: 12345678
```

## Cargar datos

En Neo4j Browser, copiar y ejecutar el contenido de:

```text
script-carga-datos.cypher
```

Ese script crea usuarios, intereses, publicaciones y relaciones:

```text
(:Usuario)-[:AMIGO_DE]-(:Usuario)
(:Usuario)-[:PUBLICA]->(:Publicacion)
(:Usuario)-[:LE_GUSTA]->(:Publicacion)
(:Usuario)-[:TIENE_INTERES]->(:Interes)
```

## Ejecutar la aplicacion

Con Neo4j arrancado:

```bash
mvn compile exec:java
```

La conexion se configura en:

```text
src/main/resources/application.properties
```

Tambien se puede sobrescribir con variables de entorno:

```bash
export NEO4J_URI=neo4j://localhost:7687
export NEO4J_USER=neo4j
export NEO4J_PASSWORD=12345678
```

## Interfaz grafica

La aplicacion abre una ventana con cinco pestanas:

- Usuarios: crear, listar, actualizar y borrar usuarios.
- Publicaciones: crear publicaciones asociadas a un autor, actualizar y borrar.
- Intereses: crear, renombrar, listar y borrar intereses.
- Relaciones: crear/eliminar amistades, asignar intereses y dar likes.
- Consultas: amigos sugeridos, intereses comunes, publicaciones con mas likes,
  camino mas corto y comunidades por ciudad.

## Archivos de entrega

```text
docker-compose.yml
script-carga-datos.cypher
consultas-crud.cypher
consultas-complejas.cypher
README.md
```

## Estructura

```text
model/        Clases de datos: Usuario, Publicacion, Interes
repository/   Consultas Cypher contra Neo4j
controller/   Operaciones usadas por la interfaz
view/         Interfaz grafica Swing
```
