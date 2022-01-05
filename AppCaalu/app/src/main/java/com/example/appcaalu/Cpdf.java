package com.example.appcaalu;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cpdf extends AppCompatActivity {
    EditText nombrepersonal, grupo;
    int pageWidth = 1200;
    Date dateobj;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpdf);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        //Asignacion
        nombrepersonal = findViewById(R.id.ednombrepers);
        grupo = findViewById(R.id.edgrupo);


    }


    public void BCrear(View view){

        dateobj = new Date();

        if (nombrepersonal.getText().toString().length()==0 ||
        grupo.getText().toString().length()==0){
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_LONG).show();
        }else {
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            Paint title = new Paint();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            title.setTextAlign(Paint.Align.CENTER);
            title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            title.setTextSize(70);
            canvas.drawText("Control de asistencia de alumnos ", pageWidth/2, 270, title);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(35f);
            paint.setColor(Color.BLACK);
            canvas.drawText(nombrepersonal.getText().toString(),20,590, paint);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(grupo.getText().toString(),pageWidth-20,590,paint);

            dateFormat = new SimpleDateFormat("dd/MM/yy");
            canvas.drawText("Fecha: " + dateFormat.format(dateobj),pageWidth-20, 640, paint);

            paint.setStyle((Paint.Style.STROKE));
            paint.setStrokeWidth(2);
            canvas.drawRect(20,780,pageWidth-20,860,paint);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("No.control",70,830,paint);
            canvas.drawText("Nombre(s)", 110,830,paint);
            canvas.drawText("Ap.Paterno", 150,830,paint);
            canvas.drawText("Ap.Materno", 190,830,paint);
            canvas.drawText("Hr.Entrada", 220,830,paint);
            canvas.drawText("T.Entrada", 250,830,paint);
            canvas.drawText("Sintomas", 290,830,paint);
            canvas.drawText("Contacto", 330,830,paint);
            canvas.drawText("Hr.Salida", 360,830,paint);
            canvas.drawText("T.Salida", 390,830,paint);
            canvas.drawText("Observaciones", 480,830,paint);

            canvas.drawLine(90,790,90,840,paint);
            canvas.drawLine(140,790,140,840,paint);
            canvas.drawLine(180,790,180,840,paint);
            canvas.drawLine(210,790,210,840,paint);
            canvas.drawLine(240,790,240,840,paint);
            canvas.drawLine(280,790,280,840,paint);
            canvas.drawLine(320,790,320,840,paint);
            canvas.drawLine(350,790,350,840,paint);
            canvas.drawLine(380,790,380,840,paint);
            canvas.drawLine(470,790,470,840,paint);

            pdfDocument.finishPage(page);

            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File documento = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(documento, nombrepersonal.getText().toString() + grupo.getText().toString() +
                    dateFormat.format(dateobj) + ".pdf");


            try {
               // path.mkdirs();
               //
                // pdfDocument.writeTo(new FileOutputStream(file));
                File yourFile = new File("score.txt");
                yourFile.mkdirs(); // if file already exists will do nothing
                FileOutputStream oFile = new FileOutputStream(yourFile, false);
            }catch (IOException e){
                e.printStackTrace();
            }
            pdfDocument.close();
        }

       // Toast.makeText(this, "Btoton crear", Toast.LENGTH_SHORT).show();
    }

    //Método para obtener el directorio



    public void BVisualizar(View view){
        Toast.makeText(this, "Boton visualizar", Toast.LENGTH_SHORT).show();

    }

    public void BRegresar(View view){
        Toast.makeText(this, "Regresar a generar", Toast.LENGTH_SHORT).show();
startActivity(new Intent(this, Generar.class));
    }
}