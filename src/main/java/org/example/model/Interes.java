package org.example.model;

public class Interes {
    private String nombre;

    public Interes(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Interes{" + "nombre=" + nombre + '}';
    }
}
