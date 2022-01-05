package com.example.appcaalu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Registro extends AppCompatActivity {
    //Inicializar variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dbreference, dbrefalum;
    DrawerLayout drawerLayout;
    Button mDatePickerBtn, btnTimePickerEntrada, btnTimePickerSalida;
    EditText nregistro, ncontrol, nombreal, apaterno, amaterno, grp,
            sintms, contc, temingreso, temsalida, observac;
    Spinner spinsintomas, spincontacto;
    int hrentrada, hrsalida, minentrada, minsalida;
    String[] sintom = {"¿Tienes sintomas de COVID-19?", "Si", "No"};
    String[] contact = {"¿Contacto con una persona con COVID-19?", "Si", "No"};
    Boolean isFirstTimeC = true, isFirstTimeS = true;
    ClaseRegistro registroSelected;
    ClaseAlumnos alumnoSelected;
    ArrayList<ClaseRegistro> listRegistro;
    ArrayList<ClaseAlumnos> listAlumnosr;
    RecyclerView rvregistro, rvalumnor;
    SearchView buscqregistro, busqalumno;
    adapter_registro adapter_regist;
    adapter_alumnos adapter_alumnosr;
    LinearLayoutManager llmregistro, llmalumnor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Asignacion de variable para la barra de navegacion
        drawerLayout = findViewById(R.id.drawer_layout);

        //Asignacion del DatePicker fecha
        mDatePickerBtn = findViewById(R.id.fech);

        //Asignacion del TimePicker a la hora de entrada/ingreso
        btnTimePickerEntrada = findViewById(R.id.horaentrada);

        //Asignacion del spinner de sintomas
        spinsintomas = findViewById(R.id.spinnersintomas);

        //Asignacion del spinner de contacto
        spincontacto = findViewById(R.id.spinnercontacto);

        //Asignacion del TimePicker a la hora de entrada/ingreso
        btnTimePickerSalida = findViewById(R.id.horasalida);

        //Asignaciones de los EditText del registro
        nregistro = findViewById(R.id.regis);
        ncontrol = findViewById(R.id.nocontrol);
        nombreal = findViewById(R.id.nombre);
        apaterno = findViewById(R.id.appaterno);
        amaterno = findViewById(R.id.apmaterno);
        grp = findViewById(R.id.grupoalmn);
        temingreso = findViewById(R.id.tempentrada);
        sintms = findViewById(R.id.spsintom);
        contc = findViewById(R.id.spcontact);
        temsalida = findViewById(R.id.tempsalida);
        observac = findViewById(R.id.edobservaciones);

        //Inicializar firebase
        inicializarFirebase();

        //Asignacion de valores a variables con el calendario
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Método para mostrar el calendario
        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Registro.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                String fecha = day + "/" + month + "/" + year;
                                mDatePickerBtn.setText(fecha);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        //Lista desplegable para saber si padece sintomas usando un Array para un Spinner
        ArrayAdapter adptsintomas = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sintom);
        spinsintomas.setAdapter(adptsintomas);
        spinsintomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeS) {
                    isFirstTimeS = false;
                } else {
                    Toast.makeText(Registro.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    sintms.setText(spinsintomas.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Registro.this, "Debes seleccionar una respuesta", Toast.LENGTH_SHORT).show();
            }
        });

        //Lista desplegable para saber si ha estado en contacto usando un Array para un Spinner
        ArrayAdapter adptcontacto = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, contact);
        spincontacto.setAdapter(adptcontacto);
        spincontacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeC) {
                    isFirstTimeC = false;
                } else {
                    Toast.makeText(Registro.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    contc.setText(spincontacto.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Asignación para realizar la busqueda de registro
        dbreference = FirebaseDatabase.getInstance().getReference().child("Registro");
        rvregistro = findViewById(R.id.rv_registros);
        buscqregistro = findViewById(R.id.buscaregistro);
        llmregistro = new LinearLayoutManager(this);
        rvregistro.setLayoutManager(llmregistro);
        listRegistro = new ArrayList<>();
        adapter_regist = new adapter_registro(listRegistro);
        rvregistro.setAdapter(adapter_regist);

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ClaseRegistro claRegistro = dataSnapshot.getValue(ClaseRegistro.class);
                        listRegistro.add(claRegistro);
                    }
                    adapter_regist.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        buscqregistro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String n) {
                buscarreg(n);
                return true;
            }
        });

        //Asignación para realizar la busqueda de alumnos
        dbrefalum = FirebaseDatabase.getInstance().getReference().child("Alumnos");
        rvalumnor = findViewById(R.id.rv_alumno);
        busqalumno = findViewById(R.id.buscaalumnos);
        llmalumnor = new LinearLayoutManager(this);
        rvalumnor.setLayoutManager(llmalumnor);
        listAlumnosr = new ArrayList<>();
        adapter_alumnosr = new adapter_alumnos(listAlumnosr);
        rvalumnor.setAdapter(adapter_alumnosr);

        dbrefalum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datasnpst : snapshot.getChildren()) {
                        ClaseAlumnos claalumns = datasnpst.getValue(ClaseAlumnos.class);
                        listAlumnosr.add(claalumns);
                    }
                    adapter_alumnosr.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        busqalumno.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String a) {
                buscalumnor(a);
                return true;
            }
        });

    } //Fin del onCreate

    //Método que se encarga de mostrar la lista con fecha, grupo y nombre a traves de una busqueda
    private void buscarreg(String n) {
        ArrayList<ClaseRegistro> lista = new ArrayList<>();
        for (ClaseRegistro objeto : listRegistro) {
            if (objeto.getFecha().toLowerCase().contains(n.toLowerCase())
                    || objeto.getGrupo().toLowerCase().contains(n.toLowerCase())
                    || objeto.getNombre().toLowerCase().contains(n.toLowerCase())) {
                lista.add(objeto);
            }
        }
        adapter_registro adapter_regist = new adapter_registro(lista);
        rvregistro.setAdapter(adapter_regist);

        //Método para enviar los datos de seleccion en el CardView a los TextView
        adapter_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {
                registroSelected = lista.get(rvregistro.getChildAdapterPosition(r));
                nregistro.setText(registroSelected.getNoregistro());
                mDatePickerBtn.setText(registroSelected.getFecha());
                ncontrol.setText(registroSelected.getNocontrol());
                nombreal.setText(registroSelected.getNombre());
                apaterno.setText(registroSelected.getApellidopaterno());
                amaterno.setText(registroSelected.getApellidomaterno());
                grp.setText(registroSelected.getGrupo());
                btnTimePickerEntrada.setText(registroSelected.getHoraingreso());
                temingreso.setText(registroSelected.getTemperingreso());
                sintms.setText(registroSelected.getSintomas());
                contc.setText(registroSelected.getContacto());
                btnTimePickerSalida.setText(registroSelected.getHorasalida());
                temsalida.setText(registroSelected.getTempersalida());
                observac.setText(registroSelected.getObservaciones());
            }
        });
    }

    private void buscalumnor(String a) {
        ArrayList<ClaseAlumnos> listaalum = new ArrayList<>();
        for (ClaseAlumnos objalmr : listAlumnosr) {
            if (objalmr.getNombres().toLowerCase().contains(a.toLowerCase())
                    || objalmr.getApellidopaterno().toLowerCase().contains(a.toLowerCase())
                    || objalmr.getApellidomaterno().toLowerCase().contains(a.toLowerCase())) {
                listaalum.add(objalmr);
            }
        }
        adapter_alumnos adapter_alumnosr = new adapter_alumnos(listaalum);
        rvalumnor.setAdapter(adapter_alumnosr);

        //Método para enviar los datos de seleccion en el CardView a los TextView
        adapter_alumnosr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alumnoSelected = listaalum.get(rvalumnor.getChildAdapterPosition(v));
                ncontrol.setText(alumnoSelected.getNocontrol());
                nombreal.setText(alumnoSelected.getNombres());
                apaterno.setText(alumnoSelected.getApellidopaterno());
                amaterno.setText(alumnoSelected.getApellidomaterno());
                grp.setText(alumnoSelected.getGrupo());
            }
        });
    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Método para mostrar un TimePicker y seleccione la hora de entrada del alumno
    public void HrEntrada(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selechoraentrada, int selecminutentrada) {
                hrentrada = selechoraentrada;
                minentrada = selecminutentrada;
                btnTimePickerEntrada.setText(String.format(Locale.getDefault(), "%02d:%02d", hrentrada, minentrada));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, onTimeSetListener, hrentrada, minentrada, true);

        timePickerDialog.setTitle("Selecciona la hora");
        timePickerDialog.show();
    }

    //Método para mostrar un TimePicker y seleccione la hora de salida del alumno
    public void HrSalida(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selechorasalida, int selecminutsalida) {
                hrsalida = selechorasalida;
                minsalida = selecminutsalida;
                btnTimePickerSalida.setText(String.format(Locale.getDefault(), "%02d:%02d", hrsalida, minsalida));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, onTimeSetListener, hrsalida, minsalida, true);

        timePickerDialog.setTitle("Selecciona la hora");
        timePickerDialog.show();
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
        //Recreate activity
        recreate();
    }

    public void ClickAlumnos(View view) {
        //Redirect activity to Alumnos
        MainActivity2.redirectActivity(this, Alumnos.class);
    }

    public void ClickGenerar(View view) {
        //Redirect activity to Generar
        MainActivity2.redirectActivity(this, Generar.class);
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

    //Método para limpiar los TextView
    private void limpiarR() {
        nregistro.setText("");
        mDatePickerBtn.setText("SELECCIONA LA FECHA");
        ncontrol.setText("");
        nombreal.setText("");
        apaterno.setText("");
        amaterno.setText("");
        grp.setText("");
        btnTimePickerEntrada.setText("SELECCIONA LA HORA DE INGRESO");
        temingreso.setText("");
        sintms.setText("SINTOMAS");
        contc.setText("CONTACTO");
        btnTimePickerSalida.setText("SELECCIONA LA HORA DE SALIDA");
        temsalida.setText("");
        observac.setText("");
    }

    //Método para habiitar los TextView
    private void habilitarR() {
        mDatePickerBtn.setEnabled(true);
        btnTimePickerEntrada.setEnabled(true);
        temingreso.setEnabled(true);
        btnTimePickerSalida.setEnabled(true);
        temsalida.setEnabled(true);
        observac.setEnabled(true);
    }

    //Método para deshabiitar los TextView
    private void deshabilitarR() {
        nregistro.setEnabled(false);
        mDatePickerBtn.setEnabled(false);
        ncontrol.setEnabled(false);
        nombreal.setEnabled(false);
        apaterno.setEnabled(false);
        amaterno.setEnabled(false);
        grp.setEnabled(false);
        btnTimePickerEntrada.setEnabled(false);
        temingreso.setEnabled(false);
        btnTimePickerSalida.setEnabled(false);
        temsalida.setEnabled(false);
        observac.setEnabled(false);
    }

    //Método Validación usado en el BotónGuardar, cuando los EditText esten vacios indicar error
    private void validacionR() {
        String numcontrol = ncontrol.getText().toString();
        String nnombre = nombreal.getText().toString();
        String apdpaterno = apaterno.getText().toString();
        String apdmaterno = amaterno.getText().toString();
        String group = grp.getText().toString();
        String tingreso = temingreso.getText().toString();
        String sinto = spinsintomas.getSelectedItem().toString();
        String cont = spincontacto.getSelectedItem().toString();

        if (numcontrol.equals("")) {
            ncontrol.setError("Required");
        } else if (nnombre.equals("")) {
            nombreal.setError("Required");
        } else if (apdpaterno.equals("")) {
            apaterno.setError("Required");
        } else if (apdmaterno.equals("")) {
            amaterno.setError("Required");
        } else if (group.equals("")) {
            grp.setError("Required");
        } else if (tingreso.equals("")) {
            temingreso.setError("Required");
        }else if (sinto.equals("")){
            sintms.setError("Requiered");
        }else if (cont.equals("")){
            contc.setError("Required");
        }
    }

    //Método para el botón Nuevo para la interfaz
    public void BNuevoR(View view) {
        limpiarR();
        habilitarR();
        Toast.makeText(this, "Llena los espacios para el registro", Toast.LENGTH_SHORT).show();
    }

    //Método para el botón Guardar para la interfaz
    public void BGuardarR(View view) {
        ClaseRegistro cregistro = new ClaseRegistro();
        String fech = mDatePickerBtn.getText().toString();
        String numcontrol = ncontrol.getText().toString();
        String nnombre = nombreal.getText().toString();
        String apdpaterno = apaterno.getText().toString();
        String apdmaterno = amaterno.getText().toString();
        String group = grp.getText().toString();
        String hringreso = btnTimePickerEntrada.getText().toString();
        String tingreso = temingreso.getText().toString();
        String sinto = spinsintomas.getSelectedItem().toString();
        String cont = spincontacto.getSelectedItem().toString();
        String hsalida = btnTimePickerSalida.getText().toString();
        String tsalida = temsalida.getText().toString();
        String observa = observac.getText().toString();

        if (numcontrol.equals("")||nnombre.equals("")||apdpaterno.equals("")||
                apdmaterno.equals("")||group.equals("")||tingreso.equals("")||
                sintms.equals("")||contc.equals("")){
            validacionR();
        } else {
            cregistro.setNoregistro(String.valueOf(UUID.randomUUID()));
            cregistro.setFecha(fech);
            cregistro.setNocontrol(numcontrol);
            cregistro.setNombre(nnombre);
            cregistro.setApellidopaterno(apdpaterno);
            cregistro.setApellidomaterno(apdmaterno);
            cregistro.setGrupo(group);
            cregistro.setHoraingreso(hringreso);
            cregistro.setTemperingreso(tingreso);
            cregistro.setSintomas(sinto);
            cregistro.setContacto(cont);
            cregistro.setHorasalida(hsalida);
            cregistro.setTempersalida(tsalida);
            cregistro.setObservaciones(observa);
            databaseReference.child("Registro").child(cregistro.getNoregistro()).setValue(cregistro);
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
            limpiarR();
            deshabilitarR();
        }
    }

    //Método para el botón Editar para la interfaz
    public void BEditarR(View view) {
        String regist = nregistro.getText().toString();
        String fech = mDatePickerBtn.getText().toString();
        String numcontrol = ncontrol.getText().toString();
        String nnombre = nombreal.getText().toString();
        String apdpaterno = apaterno.getText().toString();
        String apdmaterno = amaterno.getText().toString();

        if (regist.equals("") || fech.equals("") || numcontrol.equals("") || nnombre.equals("")
                || apdpaterno.equals("") || apdmaterno.equals("")) {
            Toast.makeText(this, "Selecciona un registro para editar", Toast.LENGTH_SHORT).show();
        } else {
            habilitarR();
        }
    }

    public void BEliminarR(View view) {
        String regist = nregistro.getText().toString();
        String fech = mDatePickerBtn.getText().toString();
        String numcontrol = ncontrol.getText().toString();
        String nnombre = nombreal.getText().toString();
        String apdpaterno = apaterno.getText().toString();
        String apdmaterno = amaterno.getText().toString();

        if (regist.equals("") || fech.equals("") || numcontrol.equals("") || nnombre.equals("")
                || apdpaterno.equals("") || apdmaterno.equals("")) {
            Toast.makeText(this, "Selecciona un registro para eliminar", Toast.LENGTH_SHORT).show();
        } else {
            ClaseRegistro clregistro = new ClaseRegistro();
            clregistro.setNocontrol(registroSelected.getNoregistro());
            databaseReference.child("Registro").child(clregistro.getNocontrol()).removeValue();
            Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
            limpiarR();
        }
    }

    //Método para el botón Actualizar para la interfaz
    public void BActualizarR(View view) {
        ClaseRegistro cregistroas = new ClaseRegistro();
        String regist = nregistro.getText().toString();
        String fech = mDatePickerBtn.getText().toString();
        String numcontrol = ncontrol.getText().toString();
        String nnombre = nombreal.getText().toString();
        String apdpaterno = apaterno.getText().toString();
        String apdmaterno = amaterno.getText().toString();
        if (regist.equals("") || fech.equals("") || numcontrol.equals("") || nnombre.equals("")
                || apdpaterno.equals("") || apdmaterno.equals(""))  {
            Toast.makeText(this, "Selecciona un registro y despúes el botón editar para actualizar",
                    Toast.LENGTH_SHORT).show();
        } else {
            cregistroas.setNoregistro(registroSelected.getNoregistro());
            cregistroas.setFecha(mDatePickerBtn.getText().toString().trim());
            cregistroas.setNocontrol(ncontrol.getText().toString().trim());
            cregistroas.setNombre(nombreal.getText().toString().trim());
            cregistroas.setApellidopaterno(apaterno.getText().toString().trim());
            cregistroas.setApellidomaterno(amaterno.getText().toString().trim());
            cregistroas.setGrupo(grp.getText().toString().trim());
            cregistroas.setHoraingreso(btnTimePickerEntrada.getText().toString().trim());
            cregistroas.setTemperingreso(temingreso.getText().toString().trim());
            cregistroas.setSintomas(sintms.getText().toString().trim());
            cregistroas.setContacto(contc.getText().toString().trim());
            cregistroas.setHorasalida(btnTimePickerSalida.getText().toString().trim());
            cregistroas.setTempersalida(temsalida.getText().toString().trim());
            cregistroas.setObservaciones(observac.getText().toString().trim());
            databaseReference.child("Registro").child(cregistroas.getNoregistro()).setValue(cregistroas);
            Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
            limpiarR();
            deshabilitarR();
        }
    }
}