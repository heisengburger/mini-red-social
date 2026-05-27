package org.example.repository;

import org.example.model.Interes;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InteresRepository {
    private final Driver driver;

    public InteresRepository(Driver driver) {
        this.driver = driver;
    }

    public void crear(Interes interes) {
        driver.executableQuery("""
                CREATE (:Interes {
                    nombre: $nombre
                })
                """)
                .withParameters(Map.of("nombre", interes.getNombre()))
                .execute();
    }

    public List<Interes> listar() {
        return driver.executableQuery("""
                MATCH (i:Interes)
                RETURN i.nombre AS nombre
                ORDER BY i.nombre
                """)
                .execute()
                .records()
                .stream()
                .map(record -> new Interes(record.get("nombre").asString()))
                .toList();
    }

    public Optional<Interes> buscarPorNombre(String nombre) {
        List<Interes> intereses = driver.executableQuery("""
                MATCH (i:Interes {nombre: $nombre})
                RETURN i.nombre AS nombre
                LIMIT 1
                """)
                .withParameters(Map.of("nombre", nombre))
                .execute()
                .records()
                .stream()
                .map(record -> new Interes(record.get("nombre").asString()))
                .toList();

        return intereses.stream().findFirst();
    }

    public void actualizarNombre(String nombreActual, String nombreNuevo) {
        driver.executableQuery("""
                MATCH (i:Interes {nombre: $nombreActual})
                SET i.nombre = $nombreNuevo
                """)
                .withParameters(Map.of(
                        "nombreActual", nombreActual,
                        "nombreNuevo", nombreNuevo
                ))
                .execute();
    }

    public void eliminar(String nombre) {
        driver.executableQuery("""
                MATCH (i:Interes {nombre: $nombre})
                DETACH DELETE i
                """)
                .withParameters(Map.of("nombre", nombre))
                .execute();
    }
}
