package com.example.appcaalu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //Inicializar variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText correoi, contrasenai;
    Button btnRegistrarse, btnIniciarSes;
    SwitchCompat switchtema;
    CheckBox checkGuardarSesion;
    TextView modo;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String llave="sesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Elección de modo claro y nocturno en el Login para toda la aplicación, elige el tipo de tema
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO){
            setTheme(R.style.Theme_AppCompat_Light);
        }else{
            setTheme(R.style.Theme_AppCompat_DayNight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asignacion de variable
        btnRegistrarse = findViewById(R.id.regist);
        btnIniciarSes = findViewById(R.id.inic);
        correoi = findViewById(R.id.edcorreoinicio);
        contrasenai = findViewById(R.id.edcontrasenainicio);
        switchtema = findViewById(R.id.switchcolortema);
        modo = findViewById(R.id.modocn);
        checkGuardarSesion = findViewById(R.id.guardasesion);

        mAuth = FirebaseAuth.getInstance();

        //Asignacion de variable para la guardar la sesion
        preferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (revisarSesion()){
            startActivity(new Intent(Login.this, MainActivity2.class));
        }else{
            Toast.makeText(getApplicationContext(), "Inicia sesion", Toast.LENGTH_SHORT).show();
        }

        //Boton registrarse
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Login.this, registrarse.class);
                startActivity(intent);
            }
        });

        //Switch para elegir el tema de fondo, cuando sea chequeado
        switchtema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Condition
                if (isChecked){
                    //Cuando el switch se presione entrará en modo claro/blanco
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    modo.setText("Modo Claro");
                }else {
                    //Cuando el switch se desfije cambiara al modo oscuro/nocturno
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    modo.setText("Modo Oscuro");
                }
            }
        });
    }//Fin del oncreate

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Método de inicio de sesión, indicar campos vacios además de verificar la exitencia del usuario
    public void iniciarSesion(View view) {
        String incorreo = correoi.getText().toString();
        String incontrasenia = contrasenai.getText().toString();

        if (incorreo.equals("") || incontrasenia.equals("")) {
            validainiciosesion();
        } else {
            mAuth.signInWithEmailAndPassword(incorreo, incontrasenia)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                esVerificado();
                                guardarSesion(checkGuardarSesion.isChecked());
                            } else {
                                Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //Método para indicar al usuario que es un campo requerido
    public void validainiciosesion(){
        String incorreo = correoi.getText().toString();
        String incontrasenia = contrasenai.getText().toString();

        if (incorreo.equals("")){
            correoi.setError("Required");
        }else if(incontrasenia.equals("")){
            contrasenai.setError("Required");
        }
    }

    //Método que se encarga de saber si es clickeado el checkbox
    private boolean revisarSesion(){
        return this.preferences.getBoolean(llave,false);
    }

    //Guardar la sesion del usuario
    private void guardarSesion(boolean checked){
        editor.putBoolean(llave,checked);
        editor.apply();
    }

    //Método que se encarga de sar acceso o no, de acuerdo al correo enviado
    void esVerificado(){
        FirebaseUser firebaseUser =  mAuth.getCurrentUser();
        Boolean flag=firebaseUser.isEmailVerified();
        if(flag){
            Toast.makeText(getApplicationContext(), "Acceso correcto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this,MainActivity2.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Verifica tu correo", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    //Método para salir de la aplicación
    public void ClickSalirLogin(View view){
        salir(this);
    }

    //Método que envía un cuadro de dialogo de alerta, preguntando al
    // usuario si desea salir o no de la aplicación
    public static void salir(Activity activity){
        //Alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Titulo
        builder.setTitle("Salir");
        //Mensaje
        builder.setMessage("¿Estás seguro de salir? ");
        //Botón de SI
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Termino de la actividad
                activity.finishAffinity();
                //Salida de la aplicación
                System.exit(0);
            }
        });
        //Botón de NO
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Se cierra el cuadro de dialogo
                dialog.dismiss();
            }
        });
        //Muestra el contenido del dalogo, de acuerdo a los botones anteriores
        builder.show();
    }
}