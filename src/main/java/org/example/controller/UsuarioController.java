package org.example.controller;

import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void crearUsuario(int id, String nombre, int edad, String ciudad) {
        repository.crear(new Usuario(id, nombre, edad, ciudad));
    }

    public List<Usuario> listarUsuarios() {
        return repository.listar();
    }

    public Optional<Usuario> buscarUsuarioPorId(int id) {
        return repository.buscarPorId(id);
    }

    public void actualizarUsuario(int id, String nombre, int edad, String ciudad) {
        repository.actualizar(new Usuario(id, nombre, edad, ciudad));
    }

    public void eliminarUsuario(int id) {
        repository.eliminar(id);
    }
}
