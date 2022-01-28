package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;



import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class homepage_tutores extends AppCompatActivity {
    private RequestQueue requestQueue;
    private int id_estudiante=0;
    private String ENDPOINT_FAVORITOS="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores-favoritos?id=%d";
    private String ENDPOINT_DISPONIBLES="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores?select=10";
    private String ENDPOINT_ESTUDIANTE="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/estudiantes?id=%d";
    //lista de tutores favoritos y/o totales
    List<TutoresFavoritos> lista_tutores= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_tutores);
        try{
            Intent intent= getIntent();
            id_estudiante=Integer.parseInt(intent.getStringExtra("id_estudiante"));
        }catch (Exception e){
            id_estudiante=1052;
        }
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        get_perfil_estudiante();

        get_tutores_favoritos_response(new VolleyCallback() {
            @Override
            public void onSuccess() {
                if(lista_tutores.size()>0){
                    System.out.println("Tutores favoritos disponibles");
                    System.out.println("AGREGANDO AL ACTIVITY");
                    build_items_tutores(lista_tutores);
                    //lista_tutores.clear();

                }else{
                    Toast.makeText(getApplicationContext(),"Recomendando tutores",Toast.LENGTH_SHORT).show();
                    System.out.println("NO Tiene tutores favoritos, procedemos a recomendar disponibles");
                    get_tutores_disponibles_response(new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Tutores actuales="+lista_tutores.size());
                            if(lista_tutores.size()>0){
                                build_items_tutores(lista_tutores);
                                //lista_tutores.clear();
                            }else{
                                Toast.makeText(getApplicationContext(),"No hay tutores cerca de ti.",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

        //Agregamos los eventos de click lateral
        OnClickMenuBarraLateral();
        //Agregamos la menu
        OnClickMenuHomepage();
        //meni de buscar tutor
        OnClickMenuBuscarTutor();
        //menu de mostrar perfil
        OnClickMenuPerfil();



    }

    public void build_items_tutores(List<TutoresFavoritos> tutores){
        System.out.println("EJECUTANDO EL RECYCLER VIEW");
        ListAdapter listAdapter= new ListAdapter(tutores, homepage_tutores.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TutoresFavoritos item) {
                moveToPerfilTutor(item);
            }
        });
        RecyclerView recyclerView= findViewById(R.id.lista_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(homepage_tutores.this));
        recyclerView.setAdapter(listAdapter);
    }
    public void moveToPerfilTutor(TutoresFavoritos item){
        Intent intent= new Intent(homepage_tutores.this,ver_datos_tutor.class);
        intent.putExtra("id_tutor",String.valueOf(item.getId()));
        startActivity(intent);
    }
    public void get_perfil_estudiante(){
        ENDPOINT_ESTUDIANTE=String.format(ENDPOINT_ESTUDIANTE,id_estudiante);
        JsonObjectRequest estudiante_request= new JsonObjectRequest(Request.Method.GET, ENDPOINT_ESTUDIANTE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean success= response.getBoolean("success");
                    if(success){ //si el perfil del estudiante existe
                        JSONObject data= response.getJSONObject("data");
                        System.out.println("OBTENIENDO LOS DATOS DEL PERFIL DE USUARIO");
                        TextView textNombre=findViewById(R.id.id_nombre_usuario);
                        ImageView image=findViewById(R.id.id_foto_usuario);

                        textNombre.setText(data.getString("nombre_completo"));
                        Picasso.get()
                                .load("https://tinyurl.com/mwaps96p")
                                .error(R.mipmap.ic_launcher_round)
                                .into(image);
                        System.out.println("Perfil de usuario cargado");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error de conexion a la API");
            }
        });
        requestQueue.add(estudiante_request);
    }

    public void get_tutores_favoritos_response(final VolleyCallback callback){

        ENDPOINT_FAVORITOS=String.format(ENDPOINT_FAVORITOS,id_estudiante);
        System.out.println("Accediendo al endpoint: "+ENDPOINT_FAVORITOS);
        JsonObjectRequest tutores_response= new JsonObjectRequest(Request.Method.GET, ENDPOINT_FAVORITOS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id= response.getInt("id_estudiante");
                    JSONArray jsonArray= response.getJSONArray("tutores_favoritos");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj= new JSONObject(jsonArray.get(i).toString());
                        int id_tutor=obj.getInt("id_tutor");
                        String nombre_completo=obj.getString("nombre_completo");
                        String descripcion= obj.getString("descripcion");
                        String foto=obj.getString("foto");
                        TutoresFavoritos tutor=new TutoresFavoritos(nombre_completo,descripcion,foto,id_tutor);
                        lista_tutores.add(tutor );
                        System.out.println(tutor);
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
                        System.out.println("Error al acceder al API REST");

                    }
                });

        requestQueue.add(tutores_response);

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
                        String foto=obj.getString("foto");
                        TutoresFavoritos tutor=new TutoresFavoritos(nombre_completo,descripcion,foto,id_tutor);
                        lista_tutores.add(tutor );
                        System.out.println(tutor);
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
    public void OnClickMenuHomepage(){
        ImageView imgview =  findViewById(R.id.btn_home);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homepage_tutores.this,homepage_tutores.class);
                startActivity(intent);
            }
        });
    }
    public void OnClickMenuBuscarTutor(){
        ImageView imgview =  findViewById(R.id.btn_buscar);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homepage_tutores.this,buscar_tutor.class);
                startActivity(intent);
            }
        });
    }
    public void OnClickMenuPerfil(){
        ImageView imgview =  findViewById(R.id.btn_usuario);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homepage_tutores.this,homepage_tutores.class);
                startActivity(intent);
            }
        });
    }
    public void OnClickMenuBarraLateral(){
        Button buttonLateral =  findViewById(R.id.btn_lateral);

        buttonLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homepage_tutores.this,Menu.class);
                intent.putExtra("id_estudiante", String.valueOf(id_estudiante));
                startActivity(intent);
            }
        });
    }

}
interface VolleyCallback {
    void onSuccess() throws JSONException;
}


