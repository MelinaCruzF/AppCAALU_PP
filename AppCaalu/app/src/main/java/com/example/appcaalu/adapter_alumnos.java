package com.example.appcaalu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class adapter_alumnos
        extends RecyclerView.Adapter<adapter_alumnos.viewholderalumnos>
        implements View.OnClickListener{

    List<ClaseAlumnos> alumnosList;
    private View.OnClickListener listener; //Escuchador en donde realiza click

    public adapter_alumnos(List<ClaseAlumnos> alumnosList) {
        this.alumnosList = alumnosList;
    }

    @NonNull
    @NotNull
    @Override
    public viewholderalumnos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_alumno,parent,false);
        viewholderalumnos holder = new viewholderalumnos(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholderalumnos holder, int position) {
        ClaseAlumnos ca = alumnosList.get(position);
        holder.bnombre.setText(ca.getNombres());
        holder.bapaterno.setText(ca.getApellidopaterno());
        holder.bamaterno.setText(ca.getApellidomaterno());

        holder.brnombre.setText(ca.getNombres());
        holder.brappaterno.setText(ca.getApellidopaterno());
        holder.brapmaterno.setText(ca.getApellidomaterno());
    }

    @Override
    public int getItemCount() {
        return alumnosList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) { //Oyente obtiene en donde se realizo el clic
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class viewholderalumnos extends RecyclerView.ViewHolder {
        TextView bnombre, bapaterno, bamaterno; //clase alumnos
        TextView brnombre, brappaterno, brapmaterno; //clase registro

        public viewholderalumnos(@NonNull @NotNull View itemView) {
            super(itemView);

            bnombre = itemView.findViewById(R.id.buscnombre);
            bapaterno = itemView.findViewById(R.id.buscappaterno);
            bamaterno = itemView.findViewById(R.id.buscapmaterno);

            brnombre = itemView.findViewById(R.id.buscnombre);
            brappaterno = itemView.findViewById(R.id.buscappaterno);
            brapmaterno = itemView.findViewById(R.id.buscapmaterno);
        }
    }
}