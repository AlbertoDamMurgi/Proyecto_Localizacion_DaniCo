package com.example.usuario.proyecto_localizacion_danico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        DireccionesAdapter adapter = new DireccionesAdapter(getApplicationContext(),MainActivity.getPosiciones());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Toast.makeText(getApplicationContext(),"Selecciona una ubicación para verla en el mapa",Toast.LENGTH_LONG).show();

    }
}
