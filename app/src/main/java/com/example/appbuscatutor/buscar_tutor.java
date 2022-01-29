package com.example.appbuscatutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbuscatutor.adaptador.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class buscar_tutor extends AppCompatActivity implements RecyclerAdapter.RecyclerItemClick, SearchView.OnQueryTextListener {
    DrawerLayout drawerLayout;
    private RequestQueue requestQueue;
    private RecyclerView rvLista;
    private SearchView svSearch;
    private RecyclerAdapter adapter;
    private List<Tutor> items = new ArrayList<>();

    private String ENDPOINT_DISPONIBLES="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores?select=10";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_tutor);
        drawerLayout =findViewById(R.id.drawer_layout);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        get_tutores_disponibles_response(new VolleyCallback() {
            @Override
            public void onSuccess() {
                System.out.println("Tutores actuales="+items.size());
                if(items.size()>0){
                    initViews();
                    initValues();
                    initListener();
                }else{
                    Toast.makeText(getApplicationContext(),"No hay tutores cerca de ti.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initViews(){
        rvLista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);

        //change icon color
        ImageView searchIcon = svSearch.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_search_tutor));

        SearchView.SearchAutoComplete searchAutoComplete = svSearch.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);

        adapter = new RecyclerAdapter(items, buscar_tutor.this, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tutor item) {
                Intent intent= new Intent(buscar_tutor.this,ver_datos_tutor.class);
                intent.putExtra("id_tutor",String.valueOf(item.getId()));
                startActivity(intent);
            }
        });
        rvLista.setAdapter(adapter);

    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }


    public void get_tutores_disponibles_response(final VolleyCallback callback){
        System.out.println("Accediendo al endpoint: "+ENDPOINT_DISPONIBLES);
        JsonObjectRequest tutores_disp_response= new JsonObjectRequest(Request.Method.GET, ENDPOINT_DISPONIBLES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray= response.getJSONArray("tutores");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj= new JSONObject(jsonArray.get(i).toString());
                        int id_tutor=obj.getInt("id_tutor");
                        String nombre_completo=obj.getString("nombre_completo");
                        String descripcion= obj.getString("descripcion");
                        String esp = obj.getString("especialidades");
                        System.out.println(esp);
                        String foto=obj.getString("foto");
                        Tutor tutor = new Tutor(nombre_completo,descripcion, foto ,esp, id_tutor);
                        items.add(tutor);
                    }
                    callback.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error al acceder al API REST Tutores disponibes");

                    }
                });

        requestQueue.add(tutores_disp_response);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    @Override
    public void itemClick(Tutor item) {
        Intent intent = new Intent(this, ver_datos_tutor.class);
        intent.putExtra("itemDetail", item);
        startActivity(intent);
    }

    //conexion aal menu lateral++++++++++++++++++++


    public void ClickMenu(View view){
        homepage_tutores.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        homepage_tutores.openDrawer(drawerLayout);
    }
    public void ClickPerfil(View view){
        homepage_tutores.redirectActivity(this,Perfil.class);
    }
    public void ClickMisFavoritos(View view){
        homepage_tutores.redirectActivity(this,homepage_tutores.class);
    }
    public void ClickMisTutores(View view){
        homepage_tutores.redirectActivity(this,registrar_datos_tutor.class);
    }
    public void ClickBuscarTutor(View view){
        recreate();

    }
    public void ClickHistorial(View view){
        homepage_tutores.redirectActivity(this,Historial.class);
    }
    public void ClickCerrarSesion(View view){
        homepage_tutores.logout(this);
    }
    protected void onPause(){
        super.onPause();
        homepage_tutores.closeDrawer(drawerLayout);
    }
}