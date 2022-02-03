package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class terminos_condiciones extends AppCompatActivity {

    Button id_btnconf,id_btncancel;
    String id_estudiante = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id_estudiante = intent.getStringExtra("id_estudiante");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);
        id_btnconf=findViewById(R.id.id_btnconf);
        id_btncancel=findViewById(R.id.id_btncancel);
        //Para el boton CONTINUAR
        id_btnconf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(terminos_condiciones.this,registrar_datos_tutor.class);
                intent.putExtra("id_estudiante", id_estudiante);
                startActivity(intent);
            }
        });
        //Para el boton CANCELAR se regresara al menu
        id_btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modificar MainActivity por el menu
                Intent intent = new Intent(terminos_condiciones.this,homepage_tutores.class);
                startActivity(intent);
            }
        });
    }
}