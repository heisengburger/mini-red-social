MATCH (ana:Usuario {nombre:'Ana'})-[:AMIGO_DE]-(amigo)-[:AMIGO_DE]-(sugerido)
WHERE NOT (ana)-[:AMIGO_DE]-(sugerido)
AND ana <> sugerido
RETURN sugerido.nombre AS sugerencia,
count(amigo) AS amigosEnComun,
collect(amigo.nombre) AS conectadoPor
ORDER BY amigosEnComun DESC, sugerencia;

MATCH (ana:Usuario {nombre:'Ana'})-[:TIENE_INTERES]->(interes)<-[:TIENE_INTERES]-(otro:Usuario)
WHERE ana <> otro
RETURN otro.nombre AS usuario,
collect(interes.nombre) AS interesesComunes,
count(interes) AS totalInteresesComunes
ORDER BY totalInteresesComunes DESC, usuario;

MATCH (autor:Usuario)-[:PUBLICA]->(p:Publicacion)
OPTIONAL MATCH (u:Usuario)-[:LE_GUSTA]->(p)
RETURN p.id AS idPublicacion,
p.texto AS texto,
autor.nombre AS autor,
count(u) AS numeroLikes,
collect(u.nombre) AS usuariosQueDieronLike
ORDER BY numeroLikes DESC;

MATCH camino = shortestPath(
  (:Usuario {nombre:'Ana'})-[:AMIGO_DE*]-(:Usuario {nombre:'Sofia'})
)
RETURN [n IN nodes(camino) | n.nombre] AS caminoMasCorto,
length(camino) AS numeroDeSaltos;

MATCH (u:Usuario)
RETURN u.ciudad AS ciudad,
count(u) AS totalUsuarios,
collect(u.nombre) AS usuarios
ORDER BY totalUsuarios DESC;
