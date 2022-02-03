package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class verificacion_paypal extends AppCompatActivity {

    Button btnFinalizaPaypal;
    String id_estudiante = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_paypal);
        Intent intent = getIntent();
        id_estudiante = intent.getStringExtra("id_estudiante");
        btnFinalizaPaypal = (Button) findViewById(R.id.id_btn_finalizar_paypal);
        btnFinalizaPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), homepage_tutores.class);
                intent.putExtra("id_estudiante", id_estudiante);
                startActivity(intent);
            }
        });
    }
}