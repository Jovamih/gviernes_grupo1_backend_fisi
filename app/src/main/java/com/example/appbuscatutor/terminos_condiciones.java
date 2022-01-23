package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class terminos_condiciones extends AppCompatActivity {

    Button id_btnconf,id_btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);
        id_btnconf=findViewById(R.id.id_btnconf);
        id_btncancel=findViewById(R.id.id_btncancel);
        //Para el boton CONTINUAR
        id_btnconf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(terminos_condiciones.this,registrar_datos_tutor.class);
                startActivity(intent);
            }
        });
        //Para el boton CANCELAR se regresara al menu
        id_btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modificar MainActivity por el menu
                Intent intent = new Intent(terminos_condiciones.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}