package com.example.usuario.proyecto_localizacion_danico;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {
    private static ArrayList<Posiciones> posiciones = new ArrayList<Posiciones>();
    private GoogleMap mapa;
    float[] results = {100};
    Location myLocation;
    LatLng miPosicion;
    double longitude, latitude;
    private LatLng userLocation;
    private String proveedor = "";
    LocationManager manejador;
    private final LatLng Murgi = new LatLng(36.7822801, -2.815255);
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mClient;

    public static ArrayList<Posiciones> getPosiciones() {
        return posiciones;
    }

    public static void setPosiciones(ArrayList<Posiciones> posiciones) {
        MainActivity.posiciones = posiciones;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            posiciones.add(new Posiciones(new LatLng(36.775132, -2.812932), "Ayuntamiento de El Ejido"));
            posiciones.add(new Posiciones(new LatLng(36.764014, -2.800453), "Estadio Municipal de Santo Domingo"));
            posiciones.add(new Posiciones(new LatLng(36.773189, -2.805506), "El Corte Inglés"));
            posiciones.add(new Posiciones(new LatLng(36.772935, -2.820947), "Comisaría de Policía"));
            posiciones.add(new Posiciones(new LatLng(36.774416, -2.812449), "Plaza Mayor de El Ejido"));
            posiciones.add(new Posiciones(new LatLng(36.772699, -2.811029), "Auditorio de El Ejido"));
            posiciones.add(new Posiciones(new LatLng(36.769361, -2.811693), "Pabellón municipal"));
            posiciones.add(new Posiciones(Murgi, "IES Murgi"));
            posiciones.add(new Posiciones(new LatLng(36.782439, -2.814291), "IES FuenteNueva"));
        }
        //forzamos la asignacion de permisos de localizacion
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Tienes que activar los permisos primero",Toast.LENGTH_LONG).show();
            return;
        }

        myLocation = manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        //conexion a las apis de google, necesarias interfaces  ConnectionCallbacks,OnConnectionFailedListener,
        mClient = new GoogleApiClient.Builder(this)
                //lo que nos devuelve google
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //añadimos las apis que nos interesan
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                //donde se va a pintar la actividad
                .enableAutoManage(this, this)
                .build();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (mapa == null) {
            mapa = googleMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Tienes que activar los permisos primero",Toast.LENGTH_LONG).show();
                return;
            }
            mapa.setMyLocationEnabled(true);
            mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapa.addMarker(new MarkerOptions()
                    .position(Murgi)
                    .title("IES Murgi")
                    .snippet("Instituto de Educación Secundaria Murgi")
                    .icon(BitmapDescriptorFactory
                            .fromResource(android.R.drawable.ic_menu_compass))
                    .anchor(0.5f, 0.5f));
        }


        if(myLocation==null) {

            manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000000,50,this);
            myLocation=manejador.getLastKnownLocation(proveedor);



        }
        manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000000,50,this);
        myLocation=manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        miPosicion=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        //miPosicion=new LatLng(37.422,-122.084);

        CircleOptions co = new CircleOptions().center(miPosicion).radius(150).strokeColor(Color.RED).fillColor(Color.TRANSPARENT);
        Log.d("ONMAPREADY","Estás en onMapReady");
        mapa.addCircle(co);
        for(int i=0;i<posiciones.size(); i++){

            Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),posiciones.get(i).getCoordenadas().latitude,posiciones.get(i).getCoordenadas().longitude,results);
                if(results[0]<150){
                mapa.addMarker(new MarkerOptions().position(posiciones.get(i).getCoordenadas()).title(posiciones.get(i).getNombre()));
            }
            Log.d("Hola","Hola");


        }

        //mapa.setOnMapClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
        } else {
            Button btnMiPos=(Button) findViewById(R.id.button2);
            btnMiPos.setEnabled(false);
        }

    }


    public void moveCamera(View view) {
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(Murgi,15));

    }

    public void animateCamera(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapa.getMyLocation().getLatitude(),
                            mapa.getMyLocation().getLongitude()), 15));

    }


    //este metodo se lanza cuando la actividad de añadir una localizacion termina
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {

            Place place = PlacePicker.getPlace(this, data);

            if (place == null) {
                Log.i(TAG, "ningun lugar seleccionado");
                return;
            }

            posiciones.add(new Posiciones(place.getLatLng(),place.getName().toString(),place.getAddress().toString()));

            Toast.makeText(getApplicationContext(),"Posición añadida a la lista de ubicaciones",Toast.LENGTH_LONG).show();

        }
    }


    public void addMarker(View view) {

        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }

    }


    @Override public void onMapClick(LatLng puntoPulsado) {

        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    public void limpiar(View view){
        Intent i = new Intent(getApplicationContext(),ListActivity.class);
        startActivity(i);
    }

    public void limpiarMapa(){
        mapa.clear();
        onMapReady(mapa);
       // myLocation=manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged","Changed 1");
        //myLocation=manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //miPosicion=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        //mapa.addCircle(new CircleOptions().center(miPosicion).radius(250).strokeColor(Color.RED).fillColor(Color.TRANSPARENT));
        limpiarMapa();
        Log.d("onLocationChanged","Changed 2");
        //onMapReady(mapa);

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
