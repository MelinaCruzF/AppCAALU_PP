package com.example.appcaalu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Animation animacion1, animacion2, animacion3;
    TextView tituloapp, versionapp;
    ImageView logoapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Agregar animaciones
        animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);
        animacion3 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        //Asignación de variables
        logoapp = findViewById(R.id.caalulogo);
        tituloapp = findViewById(R.id.titulo);
        versionapp = findViewById(R.id.version);

        //Asignación de las animaciones a cada objeto
        logoapp.setAnimation(animacion1);
        tituloapp.setAnimation(animacion2);
        versionapp.setAnimation(animacion3);

        //Duración del Splash Screen y avanzar a la Activity Login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}