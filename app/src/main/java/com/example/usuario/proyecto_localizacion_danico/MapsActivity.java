package com.example.usuario.proyecto_localizacion_danico;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static int posicion;

    public static int getPosicion() {
        return posicion;
    }

    public static void setPosicion(int posicion) {
        MapsActivity.posicion = posicion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dir = MainActivity.getPosiciones().get(getPosicion()).getCoordenadas();
        String nom = MainActivity.getPosiciones().get(getPosicion()).getNombre();

        mMap.addMarker(new MarkerOptions().position(dir).title(nom));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dir));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((dir),17));
    }
}
