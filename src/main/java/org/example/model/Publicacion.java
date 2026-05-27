package org.example.model;

public class Publicacion {
    private int id;
    private String texto;
    private String fecha;

    public Publicacion(int id, String texto, String fecha) {
        this.id = id;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Publicacion() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
