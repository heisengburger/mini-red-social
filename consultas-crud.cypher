CREATE (:Usuario {
  id: 9,
  nombre: 'Nerea',
  edad: 19,
  ciudad: 'Arinaga'
});

MATCH (u:Usuario)
RETURN u.id AS id, u.nombre AS nombre, u.edad AS edad, u.ciudad AS ciudad
ORDER BY u.id;

MATCH (u:Usuario {nombre: 'Nerea'})
SET u.ciudad = 'Vecindario'
RETURN u.nombre AS nombre, u.ciudad AS nuevaCiudad;

MATCH (u:Usuario {nombre: 'Nerea'})
DETACH DELETE u;

MATCH (ana:Usuario {nombre:'Ana'}), (dani:Usuario {nombre:'Dani'})
CREATE (ana)-[:AMIGO_DE {desde: '2025'}]->(dani)
RETURN ana.nombre AS usuario1, dani.nombre AS usuario2;

MATCH (:Usuario {nombre:'Ana'})-[r:AMIGO_DE]-(:Usuario {nombre:'Dani'})
DELETE r;
