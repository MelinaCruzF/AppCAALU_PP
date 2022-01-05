package com.example.appcaalu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Generar extends AppCompatActivity {
    //Inicializar variables
    DrawerLayout drawerLayout;
    DatabaseReference databaseReferenceGen;
    ArrayList<ClaseRegistro> listaGen;
    RecyclerView recyclerViewGen;
    adapter_generar adapterGenerar;
    LinearLayoutManager linearLayoutManagerGen;
    EditText numregistros, grupogen;
    Button btnBuscar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar);

        //Asignacion de variable
        drawerLayout = findViewById(R.id.drawer_layout);

        //Asignación de Textview
        numregistros = findViewById(R.id.buscarnregistros);
        grupogen = findViewById(R.id.buscargrupog);

        //Botones
        btnBuscar = findViewById(R.id.buscarG);

        //Asginación tarjetas de información
        recyclerViewGen = findViewById(R.id.rv_registrosG);
        linearLayoutManagerGen = new LinearLayoutManager(this);
        recyclerViewGen.setLayoutManager(linearLayoutManagerGen);
        listaGen = new ArrayList<>();
        adapterGenerar = new adapter_generar(listaGen);
        recyclerViewGen.setAdapter(adapterGenerar);

        //Base de datos -- llamada de evento
        databaseReferenceGen = FirebaseDatabase.getInstance().getReference("Registro");

        //Método para el botón buscar, obtenga el vlaor de l caja de texto
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad = numregistros.getText().toString();
                String grupo = grupogen.getText().toString();
                if (cantidad.equals("")||grupo.equals("")){
                    validacion();
                }else{
                    Query query = FirebaseDatabase.getInstance().getReference("Registro").
                            orderByChild("grupo").equalTo(grupogen.getText().toString()).
                            limitToLast(Integer.parseInt(numregistros.getText().toString()));

                    query.addValueEventListener(valueEventListener);
                }
            }
        });


    }//Fin deloncreate

    //Método para traer datos
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshotg) {
            listaGen.clear();
            if (snapshotg.exists()) {
                for (DataSnapshot dataSnapshotg : snapshotg.getChildren()) {
                    ClaseRegistro clseRegistro = dataSnapshotg.getValue(ClaseRegistro.class);
                    listaGen.add(clseRegistro);
                }
                adapterGenerar.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    };


    public void ClickMenu(View view) {
        //Open drawer
        MainActivity2.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        //Close drawer
        MainActivity2.closeDrawer(drawerLayout);
    }

    public void ClickInicio(View view) {
        //Redirect activity to Inicio
        MainActivity2.redirectActivity(this, MainActivity2.class);
    }

    public void ClickRegistro(View view) {
        //Redirect activity to Registro
        MainActivity2.redirectActivity(this, Registro.class);
    }

    public void ClickAlumnos(View view) {
        //Redirect activity to Alumnos
        MainActivity2.redirectActivity(this, Alumnos.class);
    }

    public void ClickGenerar(View view) {
        //Recreate activity
        recreate();
    }

    public void ClickSalir(View view) {
        //Close app
        MainActivity2.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity2.closeDrawer(drawerLayout);
    }

    void limpiarGen() {
        numregistros.setText("");
        grupogen.setText("");
    }

    void habilitarGen() {
        numregistros.setEnabled(true);
        grupogen.setEnabled(true);
    }

    void validacion() {
        String cantidad = numregistros.getText().toString();
        String grupo = grupogen.getText().toString();

        if (cantidad.equals("")) {
            numregistros.setError("Required");
        } else if (grupo.equals("")) {
            grupogen.setError("Required");
        }
    }

    public void BNuevoGen(View view) {
        habilitarGen();
        limpiarGen();
        Toast.makeText(this, "Escribe la información solicitada", Toast.LENGTH_SHORT).show();
    }
}