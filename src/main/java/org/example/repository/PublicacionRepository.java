package org.example.repository;

import org.example.model.Publicacion;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PublicacionRepository {
    private final Driver driver;

    public PublicacionRepository(Driver driver) {
        this.driver = driver;
    }

    public void crear(Publicacion publicacion) {
        driver.executableQuery("""
                CREATE (:Publicacion {
                    id: $id,
                    texto: $texto,
                    fecha: $fecha
                })
                """)
                .withParameters(Map.of(
                        "id", publicacion.getId(),
                        "texto", publicacion.getTexto(),
                        "fecha", publicacion.getFecha()
                ))
                .execute();
    }

    public List<Publicacion> listar() {
        return driver.executableQuery("""
                MATCH (p:Publicacion)
                RETURN p.id AS id, p.texto AS texto, p.fecha AS fecha
                ORDER BY p.id
                """)
                .execute()
                .records()
                .stream()
                .map(record -> new Publicacion(
                        record.get("id").asInt(),
                        record.get("texto").asString(),
                        record.get("fecha").asString()
                ))
                .toList();
    }

    public Optional<Publicacion> buscarPorId(int id) {
        List<Publicacion> publicaciones = driver.executableQuery("""
                MATCH (p:Publicacion {id: $id})
                RETURN p.id AS id, p.texto AS texto, p.fecha AS fecha
                LIMIT 1
                """)
                .withParameters(Map.of("id", id))
                .execute()
                .records()
                .stream()
                .map(record -> new Publicacion(
                        record.get("id").asInt(),
                        record.get("texto").asString(),
                        record.get("fecha").asString()
                ))
                .toList();

        return publicaciones.stream().findFirst();
    }

    public void actualizar(Publicacion publicacion) {
        driver.executableQuery("""
                MATCH (p:Publicacion {id: $id})
                SET p.texto = $texto,
                    p.fecha = $fecha
                """)
                .withParameters(Map.of(
                        "id", publicacion.getId(),
                        "texto", publicacion.getTexto(),
                        "fecha", publicacion.getFecha()
                ))
                .execute();
    }

    public void eliminar(int id) {
        driver.executableQuery("""
                MATCH (p:Publicacion {id: $id})
                DETACH DELETE p
                """)
                .withParameters(Map.of("id", id))
                .execute();
    }
}
