package com.perlaaguileta.imc;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends  RecyclerView.Adapter<Adaptador.Informe>{

    Context context;
    List<Avances> historial;

    public Adaptador(Context c, ArrayList<Avances> p){
        context = c;
        historial = p;
    }

    @NonNull
    @Override
    public Informe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(//parent.getContext()).inflate(R.layout.fragment_informe,parent,false);

       // Informe holder = new Informe(v);
        return new Informe (LayoutInflater.from(context).inflate(R.layout.card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Informe holder, int position) {

        holder.imcAdapter.setText(historial.get(position).getImcAvance());
        holder.clasiAdapter.setText(historial.get(position).getClasificacionAvance());
        holder.recoAdapter.setText(historial.get(position).getRecomendacionAvance());
        //holder.imageAdater.setImageURI(avance.getImagen());

        Picasso.get().load(historial.get(position).getImagen()).into(holder.imageAdater);
    }

    @Override
    public int getItemCount() {
        return historial.size();
    }

    public static class Informe extends RecyclerView.ViewHolder{

        TextView imcAdapter;
        TextView clasiAdapter;
        TextView recoAdapter;
        ImageView imageAdater;


        public Informe(@NonNull View itemView) {
            super(itemView);
            imcAdapter = (TextView) itemView.findViewById(R.id.imc);
            clasiAdapter = (TextView) itemView.findViewById(R.id.clasificacion);
            recoAdapter = (TextView) itemView.findViewById(R.id.recomendacion);
            imageAdater = (ImageView) itemView.findViewById(R.id.imagen);

        }
    }
}
