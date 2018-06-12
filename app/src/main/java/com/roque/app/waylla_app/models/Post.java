package com.roque.app.waylla_app.models;

import java.util.Date;

public class Post extends PostId{

    private String user_uid;
    private String descripcion;
    private String fecha;
    private String hora;
    private String image_url;
    private Date timestamp;

    public Post() {
    }

    public Post(String user_uid, String descripcion, String fecha, String hora, String image_url, Date timestamp) {
        this.user_uid = user_uid;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.image_url = image_url;
        this.timestamp = timestamp;

    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
