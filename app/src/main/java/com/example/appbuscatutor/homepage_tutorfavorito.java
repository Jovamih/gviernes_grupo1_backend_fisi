package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class homepage_tutorfavorito extends AppCompatActivity {
    private int id_usuario = 2;
    private RequestQueue requestQueue;
    private String URL_TUTORES_FAVORITOS = "https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores-favoritos?id=%d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ON CREATE");

        super.onCreate(savedInstanceState);
        //Para eliminar el BAR superior
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homepage_vista_tutores);
        //obtener el Intent de la actividad actual recien creada
        Intent intent = getIntent();
        //Obtener el ID usuario  que la interfaz anterior envio
        try {
            id_usuario = Integer.parseInt(intent.getStringExtra("id_usuario"));
        } catch (Exception e) {
            id_usuario = 1;
        }
        System.out.println("Hola estoy aquia");
        requestQueue = Volley.newRequestQueue(this);

        List<TutoresFavoritos> tutores = tutoresFavoritosRequest(id_usuario);
        for (TutoresFavoritos t : tutores) {
            System.out.println(t.getNombre());
        }

        buildItems(tutores);

        //new TaskTutoresFavoritos(id_usuario).execute();
        System.out.println("TASK FINISHED");


    }

    private void initUI() {

    }

    private List<TutoresFavoritos> tutoresFavoritosRequest(int id) {
        List<TutoresFavoritos> tutores = new ArrayList<>();
        JsonObjectRequest responseTutores = new JsonObjectRequest(
                Request.Method.GET,
                String.format(URL_TUTORES_FAVORITOS, id),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id_estudiante = response.getInt("id_estudiante");
                            //el arreglo de tutors encontrados
                            JSONArray jsonArray = response.getJSONArray("tutores");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String nombre = jsonObject.getString("nombre_completo");
                                String descripcion = jsonObject.getString("descripcion");
                                String foto = jsonObject.getString("foto");
                                int id_tutor = jsonObject.getInt("id_tutor");
                                tutores.add(new TutoresFavoritos(nombre, descripcion, foto, id_tutor));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error de consulta de la API REST");
                        System.out.println(error);
                    }
                }

        );
        requestQueue.add(responseTutores);

        return tutores;
    }

    public void buildItems(List<TutoresFavoritos> tutores) {
        ListAdapter listAdapter = new ListAdapter(tutores, homepage_tutorfavorito.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TutoresFavoritos item) {
                moveToPerfilTutor(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(homepage_tutorfavorito.this));
        recyclerView.setAdapter(listAdapter);
    }

    public void moveToPerfilTutor(TutoresFavoritos item) {
        Intent intent = new Intent(homepage_tutorfavorito.this, ver_datos_tutor.class);
        intent.putExtra("id_tutor", String.valueOf(item.getId()));
        startActivity(intent);
    }
}