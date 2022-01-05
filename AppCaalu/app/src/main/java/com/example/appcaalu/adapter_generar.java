package com.example.appcaalu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class adapter_generar
        extends RecyclerView.Adapter<adapter_generar.viewholdergenerar>
        implements View.OnClickListener {

    List<ClaseRegistro> claseRegistroList;
    private View.OnClickListener listenerGenerar;

    public adapter_generar(List<ClaseRegistro> claseRegistroList) {
        this.claseRegistroList = claseRegistroList;
    }

    @NonNull
    @NotNull
    @Override
    public adapter_generar.viewholdergenerar onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vw = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_generar,parent,false);
        viewholdergenerar holdergenerar = new viewholdergenerar(vw);
        vw.setOnClickListener(this);
        return holdergenerar;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull adapter_generar.viewholdergenerar holder, int position) {
        ClaseRegistro claseRegistro = claseRegistroList.get(position);
        holder.gfecha.setText(claseRegistro.getFecha());
        holder.ggrupo.setText(claseRegistro.getGrupo());
        holder.ghrentrada.setText(claseRegistro.getHoraingreso());
        holder.gtmpentrada.setText(claseRegistro.getTemperingreso());
        holder.gnocontrol.setText(claseRegistro.getNocontrol());
        holder.gnombres.setText(claseRegistro.getNombre());
        holder.gpaterno.setText(claseRegistro.getApellidopaterno());
        holder.gmaterno.setText(claseRegistro.getApellidomaterno());
        holder.gsintom.setText(claseRegistro.getSintomas());
        holder.gcontacto.setText(claseRegistro.getContacto());
        holder.ghrsalida.setText(claseRegistro.getHorasalida());
        holder.gtmpsalida.setText(claseRegistro.getTempersalida());
        holder.gobservac.setText(claseRegistro.getObservaciones());
    }

    @Override
    public int getItemCount() {
        return claseRegistroList.size();
    }

    @Override
    public void onClick(View v) {
    }

    public class viewholdergenerar extends RecyclerView.ViewHolder {
        TextView gfecha, ggrupo, ghrentrada, gtmpentrada, gnocontrol, gnombres, gpaterno, gmaterno,
                gsintom, gcontacto, ghrsalida, gtmpsalida, gobservac;

        public viewholdergenerar(@NonNull @NotNull View itemView) {
            super(itemView);

            gfecha = itemView.findViewById(R.id.fechag);
            ggrupo = itemView.findViewById(R.id.grupog);
            ghrentrada = itemView.findViewById(R.id.entradag);
            gtmpentrada = itemView.findViewById(R.id.tentrg);
            gnocontrol = itemView.findViewById(R.id.ncontrolg);
            gnombres = itemView.findViewById(R.id.nombresg);
            gpaterno = itemView.findViewById(R.id.paternog);
            gmaterno = itemView.findViewById(R.id.maternog);
            gsintom = itemView.findViewById(R.id.sintomg);
            gcontacto = itemView.findViewById(R.id.contactog);
            ghrsalida = itemView.findViewById(R.id.salidag);
            gtmpsalida = itemView.findViewById(R.id.tsalig);
            gobservac = itemView.findViewById(R.id.observacg);
        }
    }
}
