package com.example.usuario.proyecto_localizacion_danico;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DireccionesAdapter extends RecyclerView.Adapter<DireccionesAdapter.PlaceViewHolder> {

    private Context mContext;
    private ArrayList<Posiciones> mPlaces;


    public DireccionesAdapter(Context context, ArrayList<Posiciones> places) {
        this.mContext = context;
        this.mPlaces = places;
    }


    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.esqueleto, parent, false);

        return new PlaceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        String placeName = mPlaces.get(position).getNombre().toString();
        //String placeAddress = mPlaces.get(position).getDireccion().toString();
        holder.nameTextView.setText(placeName);
       // holder.addressTextView.setText(placeAddress);
    }




    @Override
    public int getItemCount() {
        if(mPlaces==null) return 0;
        return mPlaces.size();
    }


    class PlaceViewHolder extends RecyclerView.ViewHolder  {

        TextView nameTextView;
        TextView addressTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MapsActivity.setPosicion(getAdapterPosition());
                    Intent i = new Intent(mContext,MapsActivity.class);
                    mContext.startActivity(i);

                }
            });
            nameTextView = (TextView) itemView.findViewById(R.id.nombre_text_view);
            addressTextView = (TextView) itemView.findViewById(R.id.direccion_text_view);
        }



    }
}
