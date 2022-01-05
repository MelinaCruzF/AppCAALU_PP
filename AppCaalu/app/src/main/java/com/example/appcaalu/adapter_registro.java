package com.example.appcaalu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class adapter_registro
        extends RecyclerView.Adapter<adapter_registro.viewholderregistro>
        implements View.OnClickListener {

    List<ClaseRegistro> registroList;
    private View.OnClickListener listenerregistro;

    public adapter_registro(List<ClaseRegistro> listRegistro) {
        this.registroList = listRegistro;
    }

    @NonNull
    @NotNull
    @Override
    public viewholderregistro onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_registro,parent,false);
        viewholderregistro holder = new viewholderregistro(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholderregistro holder, int position) {
        ClaseRegistro cr = registroList.get(position);
        holder.brfecha.setText(cr.getFecha());
        holder.brgrupo.setText(cr.getGrupo());
        holder.brnombre.setText(cr.getNombre());
    }

    @Override
    public int getItemCount() {
        return registroList.size();
    }

    public void setOnClickListener(View.OnClickListener listenerr){
        this.listenerregistro = listenerr;
    }

    @Override
    public void onClick(View v) {
        if(listenerregistro!=null){
            listenerregistro.onClick(v);
        }
    }

    public class viewholderregistro extends RecyclerView.ViewHolder {
        TextView brfecha, brgrupo, brnombre;

        public viewholderregistro(@NonNull @NotNull View itemView) {
            super(itemView);

            brfecha = itemView.findViewById(R.id.buscrfecha);
            brgrupo = itemView.findViewById(R.id.buscrgrupo);
            brnombre = itemView.findViewById(R.id.buscrnombre);
        }
    }
}