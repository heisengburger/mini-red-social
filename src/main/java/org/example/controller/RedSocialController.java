package org.example.controller;

import org.example.repository.RedSocialRepository;

import java.util.List;

public class RedSocialController {
    private final RedSocialRepository repository;

    public RedSocialController(RedSocialRepository repository) {
        this.repository = repository;
    }

    public void crearAmistad(int usuarioId, int amigoId, String desde) {
        repository.crearAmistad(usuarioId, amigoId, desde);
    }

    public void eliminarAmistad(int usuarioId, int amigoId) {
        repository.eliminarAmistad(usuarioId, amigoId);
    }

    public void asignarInteres(int usuarioId, String interesNombre) {
        repository.asignarInteres(usuarioId, interesNombre);
    }

    public void crearPublicacion(int usuarioId, int publicacionId, String texto, String fecha) {
        repository.crearPublicacion(usuarioId, publicacionId, texto, fecha);
    }

    public void darLike(int usuarioId, int publicacionId) {
        repository.darLike(usuarioId, publicacionId);
    }

    public List<RedSocialRepository.SugerenciaAmistad> sugerirAmigos(String nombreUsuario) {
        return repository.sugerirAmigos(nombreUsuario);
    }

    public List<RedSocialRepository.InteresComun> buscarInteresesComunes(String nombreUsuario) {
        return repository.buscarInteresesComunes(nombreUsuario);
    }

    public List<RedSocialRepository.PublicacionPopular> listarPublicacionesConLikes() {
        return repository.listarPublicacionesConLikes();
    }

    public RedSocialRepository.CaminoUsuario buscarCaminoMasCorto(String origen, String destino) {
        return repository.buscarCaminoMasCorto(origen, destino);
    }

    public List<RedSocialRepository.ComunidadCiudad> listarComunidadesPorCiudad() {
        return repository.listarComunidadesPorCiudad();
    }
}
