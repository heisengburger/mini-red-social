MATCH (n)
DETACH DELETE n;

CREATE CONSTRAINT usuario_id IF NOT EXISTS
FOR (u:Usuario) REQUIRE u.id IS UNIQUE;

CREATE CONSTRAINT publicacion_id IF NOT EXISTS
FOR (p:Publicacion) REQUIRE p.id IS UNIQUE;

CREATE CONSTRAINT interes_nombre IF NOT EXISTS
FOR (i:Interes) REQUIRE i.nombre IS UNIQUE;

CREATE
(:Usuario {id: 1, nombre: 'Ana', edad: 20, ciudad: 'Aguimes'}),
(:Usuario {id: 2, nombre: 'Luis', edad: 22, ciudad: 'Telde'}),
(:Usuario {id: 3, nombre: 'Marta', edad: 21, ciudad: 'Ingenio'}),
(:Usuario {id: 4, nombre: 'Carlos', edad: 23, ciudad: 'Las Palmas'}),
(:Usuario {id: 5, nombre: 'Sofia', edad: 20, ciudad: 'Aguimes'}),
(:Usuario {id: 6, nombre: 'Pedro', edad: 24, ciudad: 'Vecindario'}),
(:Usuario {id: 7, nombre: 'Dani', edad: 19, ciudad: 'Arinaga'}),
(:Usuario {id: 8, nombre: 'Lucia', edad: 22, ciudad: 'Telde'});

CREATE
(:Interes {nombre: 'Videojuegos'}),
(:Interes {nombre: 'Musica'}),
(:Interes {nombre: 'Futbol'}),
(:Interes {nombre: 'Cine'}),
(:Interes {nombre: 'Programacion'}),
(:Interes {nombre: 'Viajes'});

CREATE
(:Publicacion {id: 1, texto: 'Hoy empece un nuevo videojuego', fecha: '2025-05-25'}),
(:Publicacion {id: 2, texto: 'Me encanta escuchar musica mientras estudio', fecha: '2025-05-26'}),
(:Publicacion {id: 3, texto: 'Estoy aprendiendo bases de datos NoSQL', fecha: '2025-05-27'}),
(:Publicacion {id: 4, texto: 'Partido de futbol este fin de semana', fecha: '2025-05-28'}),
(:Publicacion {id: 5, texto: 'Planeando un viaje con amigos', fecha: '2025-05-29'});

MATCH
(ana:Usuario {nombre:'Ana'}),
(luis:Usuario {nombre:'Luis'}),
(marta:Usuario {nombre:'Marta'}),
(carlos:Usuario {nombre:'Carlos'}),
(sofia:Usuario {nombre:'Sofia'}),
(pedro:Usuario {nombre:'Pedro'}),
(dani:Usuario {nombre:'Dani'}),
(lucia:Usuario {nombre:'Lucia'})
CREATE
(ana)-[:AMIGO_DE {desde: '2024'}]->(luis),
(luis)-[:AMIGO_DE {desde: '2024'}]->(marta),
(marta)-[:AMIGO_DE {desde: '2025'}]->(sofia),
(ana)-[:AMIGO_DE {desde: '2023'}]->(carlos),
(carlos)-[:AMIGO_DE {desde: '2025'}]->(pedro),
(pedro)-[:AMIGO_DE {desde: '2025'}]->(dani),
(luis)-[:AMIGO_DE {desde: '2025'}]->(lucia),
(lucia)-[:AMIGO_DE {desde: '2025'}]->(sofia);

MATCH
(ana:Usuario {nombre:'Ana'}),
(luis:Usuario {nombre:'Luis'}),
(marta:Usuario {nombre:'Marta'}),
(carlos:Usuario {nombre:'Carlos'}),
(sofia:Usuario {nombre:'Sofia'}),
(pedro:Usuario {nombre:'Pedro'}),
(dani:Usuario {nombre:'Dani'}),
(lucia:Usuario {nombre:'Lucia'}),
(videojuegos:Interes {nombre:'Videojuegos'}),
(musica:Interes {nombre:'Musica'}),
(futbol:Interes {nombre:'Futbol'}),
(cine:Interes {nombre:'Cine'}),
(programacion:Interes {nombre:'Programacion'}),
(viajes:Interes {nombre:'Viajes'})
CREATE
(ana)-[:TIENE_INTERES]->(videojuegos),
(ana)-[:TIENE_INTERES]->(programacion),
(luis)-[:TIENE_INTERES]->(musica),
(luis)-[:TIENE_INTERES]->(videojuegos),
(marta)-[:TIENE_INTERES]->(cine),
(marta)-[:TIENE_INTERES]->(musica),
(carlos)-[:TIENE_INTERES]->(futbol),
(pedro)-[:TIENE_INTERES]->(futbol),
(sofia)-[:TIENE_INTERES]->(programacion),
(sofia)-[:TIENE_INTERES]->(viajes),
(dani)-[:TIENE_INTERES]->(programacion),
(dani)-[:TIENE_INTERES]->(videojuegos),
(lucia)-[:TIENE_INTERES]->(musica),
(lucia)-[:TIENE_INTERES]->(viajes);

MATCH
(ana:Usuario {nombre:'Ana'}),
(luis:Usuario {nombre:'Luis'}),
(marta:Usuario {nombre:'Marta'}),
(carlos:Usuario {nombre:'Carlos'}),
(sofia:Usuario {nombre:'Sofia'}),
(pedro:Usuario {nombre:'Pedro'}),
(dani:Usuario {nombre:'Dani'}),
(lucia:Usuario {nombre:'Lucia'}),
(p1:Publicacion {id:1}),
(p2:Publicacion {id:2}),
(p3:Publicacion {id:3}),
(p4:Publicacion {id:4}),
(p5:Publicacion {id:5})
CREATE
(ana)-[:PUBLICA]->(p1),
(luis)-[:PUBLICA]->(p2),
(marta)-[:PUBLICA]->(p3),
(carlos)-[:PUBLICA]->(p4),
(sofia)-[:PUBLICA]->(p5),
(luis)-[:LE_GUSTA]->(p1),
(marta)-[:LE_GUSTA]->(p1),
(pedro)-[:LE_GUSTA]->(p1),
(ana)-[:LE_GUSTA]->(p2),
(sofia)-[:LE_GUSTA]->(p2),
(carlos)-[:LE_GUSTA]->(p3),
(dani)-[:LE_GUSTA]->(p3),
(lucia)-[:LE_GUSTA]->(p5);

MATCH (n)
RETURN labels(n) AS tipo, count(n) AS total;
