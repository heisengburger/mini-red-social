package org.example.controller;

import org.example.model.Publicacion;
import org.example.repository.PublicacionRepository;

import java.util.List;
import java.util.Optional;

public class PublicacionController {
    private final PublicacionRepository repository;

    public PublicacionController(PublicacionRepository repository) {
        this.repository = repository;
    }

    public void crearPublicacion(int id, String texto, String fecha) {
        repository.crear(new Publicacion(id, texto, fecha));
    }

    public List<Publicacion> listarPublicaciones() {
        return repository.listar();
    }

    public Optional<Publicacion> buscarPublicacionPorId(int id) {
        return repository.buscarPorId(id);
    }

    public void actualizarPublicacion(int id, String texto, String fecha) {
        repository.actualizar(new Publicacion(id, texto, fecha));
    }

    public void eliminarPublicacion(int id) {
        repository.eliminar(id);
    }
}
