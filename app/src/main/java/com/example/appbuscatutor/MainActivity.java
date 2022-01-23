package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*Cambiar de activities sin usar botones */
    public void registrar_usuario(View view) {
        startActivity(new Intent(getApplicationContext(),registrar_usuario.class));
        finish();
    }
}