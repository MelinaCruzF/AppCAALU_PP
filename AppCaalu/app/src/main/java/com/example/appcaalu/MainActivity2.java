package com.example.appcaalu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity2 extends AppCompatActivity {
    //Inicializar variables
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Asignación de variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    //Método para llamar el menú
    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    //Método para desplegar el menú
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //Método que si se da click sobre el logo, se oculte el menú
    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    //Método para cerrar el menú
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //Cuando este abierto se debe cerrar
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //Método que se indica a si mismo, primer ventana
    public void ClickInicio(View view) {
        //Recreate activity
        recreate();
    }

    //Método que dirije a la Activity Registro
    public void ClickRegistro(View view) {
        redirectActivity(this, Registro.class);
    }

    //Método que dirije a la Activity Alumnos
    public void ClickAlumnos(View view) {
        redirectActivity(this, Alumnos.class);
    }

    //Método que dirije a la Activity Generar
    public void ClickGenerar(View view){
        redirectActivity(this, Generar.class);
    }

    //Método para salir de la aplicación
    public void ClickSalir(View view){
        logout(this);
    }

    //Método que envía un cuadro de dialogo de alerta, preguntando al
    // usuario si desea salir o no de la aplicación
    public static void logout(Activity activity){
        //Inicializa el c. dialogo de Alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Titulo
        builder.setTitle("Salir");
        //Mensaje
        builder.setMessage("¿Estás seguro de salir? ");
        //Botón de SI
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                activity.finishAffinity();

                //Exit app
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

    //Método que se encarga de enviar a la otra actividad
    public static void redirectActivity(Activity activity, Class aClass){
        //Inicializa el intent
        Intent intent = new Intent(activity,aClass);
        //Realiza el intent
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Inicia la otra activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Cierra el menú
        closeDrawer(drawerLayout);
    }
}