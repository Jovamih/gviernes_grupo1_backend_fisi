package com.example.appbuscatutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout ;
private NavigationView navigationView;
private Toolbar toolbar;

    ImageView foto;
    String foto_text;
    private String id_estudiante=null;
    private RequestQueue queue;
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        setContentView(R.layout.nav_header_main);
        //obtener el ID de la intefaz login
        Intent intent=getIntent();
        id_estudiante=intent.getStringExtra("id_estudiante");
        System.out.println("ID RECUPERADO DE homepage "+id_estudiante);
        queue = Volley.newRequestQueue(this);
        MetGet();
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        setContentView(R.layout.menu);
        //Hooks
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_menu);
        toolbar =  findViewById(R.id.toolbar);
        //toolbar
        setSupportActionBar(toolbar);
        //NavigationDrawer Menu
        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(Menu.this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { switch (item.getItemId()){
            case R.id.nav_menu:
                Toast.makeText(this, "bikes", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MisFavoritos){
            Intent intent = new Intent(Menu.this, homepage_tutores.class);
            startActivity(intent);
            return true;
        }else
        if (id == R.id.SerTutor){
            Intent intent = new Intent(Menu.this, registrar_datos_tutor.class);
            intent.putExtra("id_estudiante", id_estudiante);
            startActivity(intent);
            return true;
        }else
        if (id == R.id.BuscarTutor){
            Intent intent = new Intent(Menu.this, buscar_tutor.class);
            startActivity(intent);
            return true;
        }else
        if (id == R.id.CerrarSesion){
            Intent intent = new Intent(Menu.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //******************************************************/
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