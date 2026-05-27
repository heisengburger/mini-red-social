package org.example.repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Value;

import java.util.List;
import java.util.Map;

public class RedSocialRepository {
    private final Driver driver;

    public RedSocialRepository(Driver driver) {
        this.driver = driver;
    }

    public void crearAmistad(int usuarioId, int amigoId, String desde) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $usuarioId}), (a:Usuario {id: $amigoId})
                MERGE (u)-[r:AMIGO_DE]-(a)
                SET r.desde = $desde
                """)
                .withParameters(Map.of(
                        "usuarioId", usuarioId,
                        "amigoId", amigoId,
                        "desde", desde
                ))
                .execute();
    }

    public void eliminarAmistad(int usuarioId, int amigoId) {
        driver.executableQuery("""
                MATCH (:Usuario {id: $usuarioId})-[r:AMIGO_DE]-(:Usuario {id: $amigoId})
                DELETE r
                """)
                .withParameters(Map.of(
                        "usuarioId", usuarioId,
                        "amigoId", amigoId
                ))
                .execute();
    }

    public void asignarInteres(int usuarioId, String interesNombre) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $usuarioId})
                MERGE (i:Interes {nombre: $interesNombre})
                MERGE (u)-[:TIENE_INTERES]->(i)
                """)
                .withParameters(Map.of(
                        "usuarioId", usuarioId,
                        "interesNombre", interesNombre
                ))
                .execute();
    }

    public void crearPublicacion(int usuarioId, int publicacionId, String texto, String fecha) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $usuarioId})
                CREATE (p:Publicacion {id: $publicacionId, texto: $texto, fecha: $fecha})
                CREATE (u)-[:PUBLICA]->(p)
                """)
                .withParameters(Map.of(
                        "usuarioId", usuarioId,
                        "publicacionId", publicacionId,
                        "texto", texto,
                        "fecha", fecha
                ))
                .execute();
    }

    public void darLike(int usuarioId, int publicacionId) {
        driver.executableQuery("""
                MATCH (u:Usuario {id: $usuarioId}), (p:Publicacion {id: $publicacionId})
                MERGE (u)-[:LE_GUSTA]->(p)
                """)
                .withParameters(Map.of(
                        "usuarioId", usuarioId,
                        "publicacionId", publicacionId
                ))
                .execute();
    }

    public List<SugerenciaAmistad> sugerirAmigos(String nombreUsuario) {
        return driver.executableQuery("""
                MATCH (u:Usuario {nombre: $nombreUsuario})-[:AMIGO_DE]-(amigo)-[:AMIGO_DE]-(sugerido)
                WHERE NOT (u)-[:AMIGO_DE]-(sugerido)
                AND u <> sugerido
                RETURN sugerido.nombre AS sugerencia,
                       count(DISTINCT amigo) AS amigosEnComun,
                       collect(DISTINCT amigo.nombre) AS conectadoPor
                ORDER BY amigosEnComun DESC, sugerencia
                """)
                .withParameters(Map.of("nombreUsuario", nombreUsuario))
                .execute()
                .records()
                .stream()
                .map(record -> new SugerenciaAmistad(
                        record.get("sugerencia").asString(),
                        record.get("amigosEnComun").asInt(),
                        joinStringList(record.get("conectadoPor"))
                ))
                .toList();
    }

    public List<InteresComun> buscarInteresesComunes(String nombreUsuario) {
        return driver.executableQuery("""
                MATCH (u:Usuario {nombre: $nombreUsuario})-[:TIENE_INTERES]->(interes)<-[:TIENE_INTERES]-(otro:Usuario)
                WHERE u <> otro
                RETURN otro.nombre AS usuario,
                       collect(interes.nombre) AS interesesComunes,
                       count(interes) AS totalInteresesComunes
                ORDER BY totalInteresesComunes DESC, usuario
                """)
                .withParameters(Map.of("nombreUsuario", nombreUsuario))
                .execute()
                .records()
                .stream()
                .map(record -> new InteresComun(
                        record.get("usuario").asString(),
                        joinStringList(record.get("interesesComunes")),
                        record.get("totalInteresesComunes").asInt()
                ))
                .toList();
    }

    public List<PublicacionPopular> listarPublicacionesConLikes() {
        return driver.executableQuery("""
                MATCH (autor:Usuario)-[:PUBLICA]->(p:Publicacion)
                OPTIONAL MATCH (u:Usuario)-[:LE_GUSTA]->(p)
                RETURN p.id AS idPublicacion,
                       p.texto AS texto,
                       autor.nombre AS autor,
                       count(u) AS numeroLikes,
                       collect(u.nombre) AS usuariosQueDieronLike
                ORDER BY numeroLikes DESC, idPublicacion
                """)
                .execute()
                .records()
                .stream()
                .map(record -> new PublicacionPopular(
                        record.get("idPublicacion").asInt(),
                        record.get("texto").asString(),
                        record.get("autor").asString(),
                        record.get("numeroLikes").asInt(),
                        joinStringList(record.get("usuariosQueDieronLike"))
                ))
                .toList();
    }

    public CaminoUsuario buscarCaminoMasCorto(String origen, String destino) {
        var records = driver.executableQuery("""
                MATCH camino = shortestPath(
                    (:Usuario {nombre: $origen})-[:AMIGO_DE*]-(:Usuario {nombre: $destino})
                )
                RETURN [n IN nodes(camino) | n.nombre] AS caminoMasCorto,
                       length(camino) AS numeroDeSaltos
                """)
                .withParameters(Map.of(
                        "origen", origen,
                        "destino", destino
                ))
                .execute()
                .records();

        if (records.isEmpty()) {
            return new CaminoUsuario("", 0);
        }

        var record = records.getFirst();
        return new CaminoUsuario(
                joinStringList(record.get("caminoMasCorto")),
                record.get("numeroDeSaltos").asInt()
        );
    }

    public List<ComunidadCiudad> listarComunidadesPorCiudad() {
        return driver.executableQuery("""
                MATCH (u:Usuario)
                RETURN u.ciudad AS ciudad,
                       count(u) AS totalUsuarios,
                       collect(u.nombre) AS usuarios
                ORDER BY totalUsuarios DESC, ciudad
                """)
                .execute()
                .records()
                .stream()
                .map(record -> new ComunidadCiudad(
                        record.get("ciudad").asString(),
                        record.get("totalUsuarios").asInt(),
                        joinStringList(record.get("usuarios"))
                ))
                .toList();
    }

    private static String joinStringList(Value value) {
        return String.join(", ", value.asList(Value::asString));
    }

    public record SugerenciaAmistad(String sugerencia, int amigosEnComun, String conectadoPor) {
    }

    public record InteresComun(String usuario, String interesesComunes, int totalInteresesComunes) {
    }

    public record PublicacionPopular(int idPublicacion, String texto, String autor, int numeroLikes,
                                     String usuariosQueDieronLike) {
    }

    public record CaminoUsuario(String caminoMasCorto, int numeroDeSaltos) {
    }

    public record ComunidadCiudad(String ciudad, int totalUsuarios, String usuarios) {
    }
}
