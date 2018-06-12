package com.roque.app.waylla_app.models;

/**
 * Created by kevin on 10/06/2018.
 */

public class Usuario {

    private String user_uid;
    private String avatar;
    private int coins;
    private String descripcion;
    private String nombre;
    private int nivel;
    private int puntaje;

    public Usuario(){

    }

    public Usuario(String user_uid, String avatar, int coins, String descripcion, String nombre, int nivel, int puntaje) {
        this.user_uid = user_uid;
        this.avatar = avatar;
        this.coins = coins;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.nivel = nivel;
        this.puntaje = puntaje;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "user_uid='" + user_uid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", coins=" + coins +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nivel=" + nivel +
                ", puntaje=" + puntaje +
                '}';
    }
}
