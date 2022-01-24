package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button)findViewById(R.id.btnin);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("CLICK REALIZADO");
                Intent intent= new Intent(getBaseContext(),homepage_tutorfavorito.class);
                startActivity(intent);
            }
        });

    }

    /*Cambiar de activities sin usar botones */
    public void registrar_usuario(View view) {
        startActivity(new Intent(getApplicationContext(),registrar_usuario.class));
        finish();
    }
    public void homepage(View view){
        Intent intent= new Intent(MainActivity.this,homepage.class);
        startActivity(intent);
    }
}