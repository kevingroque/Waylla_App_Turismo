package com.roque.app.waylla_app.models;

/**
 * Created by kevin on 17/06/2018.
 */

public class Loma extends LomaId{

    private String nombre;
    private String descripcion;
    private String distrito;
    private Float elevacion;
    private String imagen;
    private Float latitud;
    private Float longitud;
    private String ubicacion;

    public Loma() {
    }

    public Loma(String nombre, String descripcion, String distrito,Float elevacion, String imagen, Float latitud, Float longitud, String ubicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.distrito = distrito;
        this.elevacion = elevacion;
        this.imagen = imagen;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getElevacion() {
        return elevacion;
    }

    public void setElevacion(Float elevacion) {
        this.elevacion = elevacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
}
