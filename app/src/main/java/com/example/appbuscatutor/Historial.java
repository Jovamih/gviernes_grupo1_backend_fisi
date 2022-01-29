package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Historial extends AppCompatActivity {
    //***************
    private String id_estudiante=null;
    private RequestQueue queue;
    //*****************
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //***********************+
        setContentView(R.layout.main_nav_drawer);
        Intent intent=getIntent();
        id_estudiante=intent.getStringExtra("id_estudiante");
        System.out.println("ID RECUPERADO DE homepage "+id_estudiante);
        queue = Volley.newRequestQueue(this);
        MetGet();
        //+++++++++++++++++++++++
        setContentView(R.layout.activity_historial);
        drawerLayout =findViewById(R.id.drawer_layout);
    }
    public void ClickMenu(View view){
        homepage_tutores.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        homepage_tutores.openDrawer(drawerLayout);
    }
    public void ClickPerfil(View view){

        Intent intent = new Intent(getApplicationContext(), Perfil.class);
        intent.putExtra("id_estudiante", String.valueOf(id_estudiante));
        startActivity(intent);
    }
    public void ClickMisFavoritos(View view){
        homepage_tutores.redirectActivity(this,homepage_tutores.class);
    }
    public void ClickMisTutores(View view){
       // homepage_tutores.redirectActivity(this,registrar_datos_tutor.class);
        Intent intent = new Intent(getApplicationContext(), registrar_datos_tutor.class);
        intent.putExtra("id_estudiante", String.valueOf(id_estudiante));
        startActivity(intent);
    }
    public void ClickBuscarTutor(View view){
        Intent intent = new Intent(getApplicationContext(),buscar_tutor.class);
        intent.putExtra("id_estudiante", String.valueOf(id_estudiante));
        startActivity(intent);

    }
    public void ClickHistorial(View view){
        recreate();

    }
    public void ClickCerrarSesion(View view){
        homepage_tutores.logout(this);
    }
    protected void onPause(){
        super.onPause();
        homepage_tutores.closeDrawer(drawerLayout);
    }
    private void MetGet(){

        String endpoint="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/estudiantes?id="+id_estudiante;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>()  {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    TextView nombre, correo;

                    nombre=(TextView)findViewById(R.id.nombre);
                    correo=(TextView)findViewById(R.id.correo);
                    JSONObject mJsonObject = response.getJSONObject("data");
                    String estado = response.getString("success");
                    if(estado == "true"){
                        nombre.setText(mJsonObject.getString("nombre_completo"));
                        correo.setText(mJsonObject.getString("correo"));
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
        queue.add(request);
    }
}