package com.example.appcaalu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class registrarse extends AppCompatActivity {
    //Inicializar variables
    private FirebaseAuth mAuth;
    private EditText correo, contrasena, confirmacontrasena;
    Button btnregresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        //Asignacion de variable
        correo = findViewById(R.id.edcorreo);
        contrasena = findViewById(R.id.edcontra);
        confirmacontrasena = findViewById(R.id.edcontraconf);
        btnregresar = findViewById(R.id.regresar);

        //Instancia para autenticación
        mAuth = FirebaseAuth.getInstance();

    }//Fin oncreate

    //Método del botón registrarse, se encuentra en la interfaz
    public void RegistrarUsuario(View view) {
       String email = correo.getText().toString();
       String password= contrasena.getText().toString();
       String confirmapassword = confirmacontrasena.getText().toString();

       if (email.equals("") || password.equals("") || confirmapassword.equals("")) {
          validacionRegistro();
       } else {
           if (contrasena.getText().toString().equals(confirmacontrasena.getText().toString())) {
               mAuth.createUserWithEmailAndPassword(email, password).
                       addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (!task.isSuccessful()) {
                                  Toast.makeText(getApplicationContext(), "Error al crear usuario",
                                          Toast.LENGTH_SHORT).show();
                                   Toast.makeText(getApplicationContext(), "La contraseña debe ser igual o mayor a ocho caracteres",
                                           Toast.LENGTH_LONG).show();
                               } else {
                                   esVerificado();
                                   limpiar();
                               }
                           }
                       });
           } else {
               Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
           }
       }
   }

   //Método del botón regresar, regresará a la pantalla del Login
    public void ClickRegresar(View view){
        Intent intent =  new Intent(this, Login.class);
        startActivity(intent);
    }

    //Método que se encarga de enviar un correo de verificación al usuario
     void esVerificado(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Correo de verificación enviado",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registrarse.this, Login.class));
                    }
                }
            });
        }
    }

    //Método que indica al usuario que los campos son requeridos
    private void validacionRegistro(){
        String email = correo.getText().toString();
        String password = contrasena.getText().toString();
        String confirmapassword = confirmacontrasena.getText().toString();

        if (email.equals("")){
            correo.setError("Required");
        }else if(password.equals("")){
            contrasena.setError("Required");
        }else if(confirmapassword.equals("")){
            confirmacontrasena.setError("Required");
        }
    }

    //Método para limpiar los campos
    void limpiar(){
        correo.setText("");
        contrasena.setText("");
        confirmacontrasena.setText("");
    }
}