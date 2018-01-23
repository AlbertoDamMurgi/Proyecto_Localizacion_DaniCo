package com.example.usuario.proyecto_localizacion_danico;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonathan on 15/01/18.
 */

public class Posiciones {
    private LatLng coordenadas;
    private String nombre;
    private String direccion;

    public Posiciones(LatLng coordenadas, String nombre) {
        this.coordenadas = coordenadas;
        this.nombre = nombre;
    }

    public Posiciones(LatLng coordenadas, String nombre, String direccion) {
        this.coordenadas = coordenadas;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Posiciones(LatLng latLng) {
    }

    public LatLng getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(LatLng coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
