package com.roque.app.waylla_app.models;


import java.util.ArrayList;

public class Evento extends EventoId{

    private String destino;
    private String titulo;
    private String hora_inicial;
    private String hora_final;
    private String fecha_inicial;
    private String fecha_final;
    private String descripcion;
    private String costo;
    private String detalle_costo;
    private String recomendaciones;
    private String requisitos;
    private String user_uid;

    public Evento(){}

    public Evento(String destino, String titulo, String hora_inicial, String hora_final, String fecha_inicial, String fecha_final, String descripcion, String costo, String detalle_costo, String recomendaciones, String requisitos, String user_uid) {
        this.destino = destino;
        this.titulo = titulo;
        this.hora_inicial = hora_inicial;
        this.hora_final = hora_final;
        this.fecha_inicial = fecha_inicial;
        this.fecha_final = fecha_final;
        this.descripcion = descripcion;
        this.costo = costo;
        this.detalle_costo = detalle_costo;
        this.recomendaciones = recomendaciones;
        this.requisitos = requisitos;
        this.user_uid = user_uid;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHora_inicial() {
        return hora_inicial;
    }

    public void setHora_inicial(String hora_inicial) {
        this.hora_inicial = hora_inicial;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getFecha_inicial() {
        return fecha_inicial;
    }

    public void setFecha_inicial(String fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getDetalle_costo() {
        return detalle_costo;
    }

    public void setDetalle_costo(String detalle_costo) {
        this.detalle_costo = detalle_costo;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
