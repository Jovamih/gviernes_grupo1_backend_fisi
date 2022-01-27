package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText txtUser, txtPass;
    Button btnlog;
    String str_email, str_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUser = findViewById(R.id.txttemail);
        txtPass = findViewById(R.id.txttpass);
        btnlog = findViewById(R.id.btnin);

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Atrapar();

            }
        });
    }

    private void Atrapar () {

        str_email = txtUser.getText().toString().trim();
        str_pass = txtPass.getText().toString().trim();

        String url = "https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/login_auth?correo="+str_email+"&password="+str_pass;

        if (txtUser.getText().toString().equals("")){
            Toast.makeText(this,"Ingrese correo",Toast.LENGTH_SHORT).show();
        }else if (txtPass.getText().toString().equals("")){
            Toast.makeText(this,"Ingrese contraseña",Toast.LENGTH_SHORT).show();
        }else{
            JsonObjectRequest postRequest = new JsonObjectRequest (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean succes = response.getBoolean("success");
                        if (succes) {
                            JSONObject data = response.getJSONObject("data");
                            String id_estudiante = String.valueOf(data.getInt("id_estudiante"));
                            Intent intent = new Intent(MainActivity.this, homepage_tutores.class);
                            intent.putExtra("id_estudiante",id_estudiante);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this,"Correo o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            Volley.newRequestQueue(this).add(postRequest);
        }



    }

    /*Cambiar de activities sin usar botones */
    public void registrar_usuario(View view) {
        startActivity(new Intent(getApplicationContext(),registrar_usuario.class));
        finish();
    }

}