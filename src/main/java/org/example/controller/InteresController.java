package org.example.controller;

import org.example.model.Interes;
import org.example.repository.InteresRepository;

import java.util.List;
import java.util.Optional;

public class InteresController {
    private final InteresRepository repository;

    public InteresController(InteresRepository repository) {
        this.repository = repository;
    }

    public void crearInteres(String nombre) {
        repository.crear(new Interes(nombre));
    }

    public List<Interes> listarIntereses() {
        return repository.listar();
    }

    public Optional<Interes> buscarInteresPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }

    public void actualizarInteres(String nombreActual, String nombreNuevo) {
        repository.actualizarNombre(nombreActual, nombreNuevo);
    }

    public void eliminarInteres(String nombre) {
        repository.eliminar(nombre);
    }
}
