 package com.example.appcaalu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

 public class Alumnos extends AppCompatActivity {
     //Inicializar variables
     FirebaseDatabase firebaseDatabase;
     DatabaseReference databaseReference, ref;
     DrawerLayout drawerLayout;
     EditText idcontrol, nombre, apepaterno, apematerno, grup, ctecnica;
     ArrayList<ClaseAlumnos> list;
     Spinner spinespecialidad;
     RecyclerView rvalumnos;
     SearchView busqalumnos;
     adapter_alumnos adaptador;
     LinearLayoutManager llm;
     String[] especialidad = {"Selecciona la especialidad","Técnico en programación",
             "Técnico en prod. ind. de alimentos"};
     ClaseAlumnos selectedalumno;
     boolean isFirstTime = true;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_alumnos);

         //Asignacion de variable
         drawerLayout = findViewById(R.id.drawer_layout);

         //Asignación del spinner de especialidad
         spinespecialidad = findViewById(R.id.spinnerespecialidad);

         //Asignacion de edittext para campos del alumno
         idcontrol = findViewById(R.id.controlalu);
         nombre = findViewById(R.id.nombrealu);
         apepaterno = findViewById(R.id.appaternoalu);
         apematerno = findViewById(R.id.apmaternoalu);
         grup = findViewById(R.id.grupoalu);
         ctecnica = findViewById(R.id.especialidadalu);

         //Inicializar Firebase
         inicializarFirebase();

         //Asignaciones para realizar la busqueda de alumnos
         ref = FirebaseDatabase.getInstance().getReference().child("Alumnos");
         rvalumnos = findViewById(R.id.rv_alumnos);
         busqalumnos = findViewById(R.id.buscaralumnos);
         llm = new LinearLayoutManager(this);
         rvalumnos.setLayoutManager(llm);
         list = new ArrayList<>();
         adaptador = new adapter_alumnos(list);
         rvalumnos.setAdapter(adaptador);

         ref.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull @NotNull DataSnapshot snapshota) {
                 if (snapshota.exists()) {
                     for (DataSnapshot dsnapshot : snapshota.getChildren()) {
                         ClaseAlumnos clalumnos = dsnapshot.getValue(ClaseAlumnos.class);
                         list.add(clalumnos);
                     }
                     adaptador.notifyDataSetChanged();
                 }
             }

             @Override
             public void onCancelled(@NonNull @NotNull DatabaseError error) {

             }
         });

         busqalumnos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String s) {
                 buscaral(s);
                 return true;
             }
         });

         //Lista desplegable para saber la especialidad un Array para un Spinner
         ArrayAdapter adptespecialidad = new ArrayAdapter(this,
                 R.layout.support_simple_spinner_dropdown_item, especialidad);
         spinespecialidad.setAdapter(adptespecialidad);
         spinespecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if (isFirstTime) {
                     isFirstTime = false;
                 } else {
                     Toast.makeText(Alumnos.this, parent.getSelectedItem().toString(),
                             Toast.LENGTH_SHORT).show();
                     ctecnica.setText(spinespecialidad.getSelectedItem().toString());
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
                 Toast.makeText(Alumnos.this, "Debes seleccionar una carrera",
                         Toast.LENGTH_SHORT).show();
             }
         });
     } //Fin del onCreate

     //Método para buscar el registro
     private void buscaral(String s) {
         ArrayList<ClaseAlumnos> milista = new ArrayList<>();
         for (ClaseAlumnos obj : list) {
             if (obj.getNombres().toLowerCase().contains(s.toLowerCase())
                     || obj.getApellidopaterno().toLowerCase().contains(s.toLowerCase())
                     || obj.getApellidomaterno().toLowerCase().contains(s.toLowerCase())) {
                 milista.add(obj);
             }
         }
         adapter_alumnos adaptador = new adapter_alumnos(milista);
         rvalumnos.setAdapter(adaptador);

         //Método para enviar los datos de seleccion en el CardView a los TextView
         adaptador.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 selectedalumno = milista.get(rvalumnos.getChildAdapterPosition(v));
                 idcontrol.setText(selectedalumno.getNocontrol());
                 nombre.setText(selectedalumno.getNombres());
                 apepaterno.setText(selectedalumno.getApellidopaterno());
                 apematerno.setText(selectedalumno.getApellidomaterno());
                 grup.setText(selectedalumno.getGrupo());
                 ctecnica.setText(selectedalumno.getEspecialidad());
             }
         });
     }

     //Funcionamiento del DrawerLayout - Menú de navegación
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
         //Recreate activity
         recreate();
     }

     public void ClickGenerar(View view) {
         //Redirect activity to Generar
         MainActivity2.redirectActivity(this, Generar.class);
     }

     public void ClickSalir(View view) {
         //Close app
         FirebaseAuth.getInstance().signOut();
         MainActivity2.logout(this);
     }

     @Override
     protected void onPause() {
         super.onPause();
         //Close drawer
         MainActivity2.closeDrawer(drawerLayout);
     }

     //Método para limpiar los TextView
     private void limpiar() {
         idcontrol.setText("");
         nombre.setText("");
         apepaterno.setText("");
         apematerno.setText("");
         grup.setText("");
         ctecnica.setText("");
     }

     //Método para habiitar los TextView
     private void habilitar() {
         idcontrol.setEnabled(true);
         nombre.setEnabled(true);
         apepaterno.setEnabled(true);
         apematerno.setEnabled(true);
         grup.setEnabled(true);
     }

     //Método habilitar TextView especificamente para el botón Editar
     private void habilitarEditar() {
         nombre.setEnabled(true);
         apepaterno.setEnabled(true);
         apematerno.setEnabled(true);
         grup.setEnabled(true);
     }

     //Método deshabilitar TextView
     private void deshabilitar() {
         idcontrol.setEnabled(false);
         nombre.setEnabled(false);
         apepaterno.setEnabled(false);
         apematerno.setEnabled(false);
         grup.setEnabled(false);
     }

     //Método para inicilizar la base de datos
     private void inicializarFirebase() {
         firebaseDatabase = FirebaseDatabase.getInstance();
         databaseReference = firebaseDatabase.getReference();
     }

     //Método para el botón Nuevo para la interfaz
     public void BNuevo(View view) {
         limpiar();
         habilitar();
         Toast.makeText(this, "Rellena los espacios en blanco", Toast.LENGTH_SHORT).show();
     }

     //Método para el botón Guardar para la interfaz
     public void BGuardar(View view) {
         ClaseAlumnos calumnosn = new ClaseAlumnos();
         String control = idcontrol.getText().toString();
         String nombrea = nombre.getText().toString();
         String apaterno = apepaterno.getText().toString();
         String amaterno = apematerno.getText().toString();
         String grpo = grup.getText().toString();
         String especc = spinespecialidad.getSelectedItem().toString();

         if (control.equals("") || nombrea.equals("") || apaterno.equals("") || amaterno.equals("")
                 || grpo.equals("") || especc.equals("")) {
             validacion();
         } else {
             calumnosn.setNocontrol(control);
             calumnosn.setNombres(nombrea);
             Toast.makeText(this, nombrea, Toast.LENGTH_SHORT).show();
             calumnosn.setApellidopaterno(apaterno);
             calumnosn.setApellidomaterno(amaterno);
             calumnosn.setGrupo(grpo);
             calumnosn.setEspecialidad(especc);
             databaseReference.child("Alumnos").child(calumnosn.getNocontrol()).setValue(calumnosn);
             Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
             limpiar();
             deshabilitar();
         }
     }

     //Método Validación usado en el BotónGuardar, cuando los EditText esten vacios indicar error
     private void validacion() {
         String control = idcontrol.getText().toString();
         String nombrea = nombre.getText().toString();
         String apaterno = apepaterno.getText().toString();
         String amaterno = apematerno.getText().toString();
         String grpo = grup.getText().toString();
         String especc = spinespecialidad.getSelectedItem().toString();

         if (control.equals("")) {
             idcontrol.setError("Required");
         } else if (nombrea.equals("")) {
             nombre.setError("Required");
         } else if (apaterno.equals("")) {
             apepaterno.setError("Required");
         } else if (amaterno.equals("")) {
             apematerno.setError("Required");
         } else if (grpo.equals("")) {
             grup.setError("Required");
         }else if(especc.equals("")){
             ctecnica.setError("Required");
         }
     }

     //Método para el botón Editar para la interfaz
     public void BEditar(View view) {
         String control = idcontrol.getText().toString();
         String nombrea = nombre.getText().toString();
         String apaterno = apepaterno.getText().toString();
         String amaterno = apematerno.getText().toString();
         String grpo = grup.getText().toString();
         String especc = spinespecialidad.getSelectedItem().toString();

         if (control.equals("") || nombrea.equals("") || apaterno.equals("") || amaterno.equals("")
                 || grpo.equals(""))  {
             Toast.makeText(this, "Selecciona un registro para editar", Toast.LENGTH_SHORT).show();
         } else {
             habilitarEditar();
         }
     }

     //Método para el botón Actualizar para la interfaz
     public void BActualizar(View view) {
         ClaseAlumnos calumnos = new ClaseAlumnos();
         String control = idcontrol.getText().toString();
         String nombrea = nombre.getText().toString();
         String apaterno = apepaterno.getText().toString();
         String amaterno = apematerno.getText().toString();
         String grpo = grup.getText().toString();
         if (control.equals("") || nombrea.equals("") || apaterno.equals("") || amaterno.equals("")
                 || grpo.equals(""))  {
             Toast.makeText(this, "Selecciona un registro y despúes el botón editar para actualizar", Toast.LENGTH_SHORT).show();
         } else {
             calumnos.setNocontrol(selectedalumno.getNocontrol());
             calumnos.setNombres(nombre.getText().toString().trim());
             calumnos.setApellidopaterno(apepaterno.getText().toString().trim());
             calumnos.setApellidomaterno(apematerno.getText().toString().trim());
             calumnos.setGrupo(grup.getText().toString().trim());
             calumnos.setEspecialidad(ctecnica.getText().toString().trim());
             databaseReference.child("Alumnos").child(calumnos.getNocontrol()).setValue(calumnos);
             Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
             limpiar();
             deshabilitar();
         }
     }

     //Método para el botón Eliminar para la interfaz
     public void BEliminar(View view) {
         String control = idcontrol.getText().toString();
         String nombrea = nombre.getText().toString();
         String apaterno = apepaterno.getText().toString();
         String amaterno = apematerno.getText().toString();
         String grpo = grup.getText().toString();
         String especc = spinespecialidad.getSelectedItem().toString();
         if (control.equals("") || nombrea.equals("") || apaterno.equals("") || amaterno.equals("")
                 || grpo.equals("")) {
             Toast.makeText(this, "Selecciona un registro para eliminar", Toast.LENGTH_SHORT).show();
         } else {
             ClaseAlumnos calumnos = new ClaseAlumnos();
             calumnos.setNocontrol(selectedalumno.getNocontrol());
             databaseReference.child("Alumnos").child(calumnos.getNocontrol()).removeValue();
             Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
             limpiar();
         }
     }
 }