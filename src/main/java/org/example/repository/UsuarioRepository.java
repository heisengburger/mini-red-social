package org.example.repository;

import org.example.model.Usuario;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UsuarioRepository {
    private final Driver driver;

    public UsuarioRepository(Driver driver) {
        this.driver = driver;
    }

    public void crear(Usuario usuario) {
        driver.executableQuery("""
                CREATE (:Usuario {
                    id: $id,
                    nombre: $nombre,
                    edad: $edad,
                    ciudad: $ciudad
                })
                """)
                .withParameters(Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "edad", usuario.getEdad(),
                        "ciudad", usuario.getCiudad()
                ))
                .execute();
    }

    public List<Usuario> listar() {
        return driver.executableQuery("""
                MATCH (u:Usuario)
                RETURN u.id AS id, u.nombre AS nombre, u.edad AS edad, u.ciudad AS ciudad
                ORDER BY u.id
                """)
                .execute()
                .records()
                .stream()
                .map(record -> new Usuario(
                        record.get("id").asInt(),
                        record.get("nombre").asString(),
                        record.get("edad").asInt(),
                        record.get("ciudad").asString()
                ))
                .toList();
    }

    public Optional<Usuario> buscarPorId(int id) {
        List<Usuario> usuarios = driver.executableQuery("""
                MATCH (u:Usuario {id: $id})
                RETURN u.id AS id, u.nombre AS nombre, u.edad AS edad, u.ciudad AS ciudad
                LIMIT 1
                """)
                .withParameters(Map.of("id", id))
                .execute()
                .records()
                .stream()
                .map(record -> new Usuario(
                        record.get("id").asInt(),
                        record.get("nombre").asString(),
                        record.get("edad").asInt(),
                        record.get("ciudad").asString()
                ))
                .toList();

        return usuarios.stream().findFirst();
    }

    public void actualizar(Usuario usuario) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $id})
                SET u.nombre = $nombre,
                    u.edad = $edad,
                    u.ciudad = $ciudad
                """)
                .withParameters(Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "edad", usuario.getEdad(),
                        "ciudad", usuario.getCiudad()
                ))
                .execute();
    }

    public void eliminar(int id) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $id})
                DETACH DELETE u
                """)
                .withParameters(Map.of("id", id))
                .execute();
    }
}
