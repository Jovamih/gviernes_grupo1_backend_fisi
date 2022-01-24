package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class homepage extends AppCompatActivity {
    private int id_usuario = 2;
    private RequestQueue requestQueue;
    private String URL_TUTORES_FAVORITOS="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores-favoritos?id=%d";

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
        try{
            id_usuario=Integer.parseInt(intent.getStringExtra("id_usuario"));
        }catch(Exception e){
            id_usuario=1;
        }
        System.out.println("Hola estoy aquia");
        requestQueue= Volley.newRequestQueue(this);

        List<TutoresFavoritos> tutores= tutoresFavoritosRequest(id_usuario);
        for(TutoresFavoritos t: tutores){
            System.out.println(t.getNombre());
        }

        buildItems(tutores);

        //new TaskTutoresFavoritos(id_usuario).execute();
        System.out.println("TASK FINISHED");


    }
    private void initUI(){

    }
    private List<TutoresFavoritos> tutoresFavoritosRequest(int id){
        List<TutoresFavoritos> tutores=new ArrayList<>();
        JsonObjectRequest responseTutores=new JsonObjectRequest(
                Request.Method.GET,
                String.format(URL_TUTORES_FAVORITOS, id),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id_estudiante= response.getInt("id_estudiante");
                            //el arreglo de tutors encontrados
                            JSONArray jsonArray= response.getJSONArray("tutores");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=new JSONObject(jsonArray.get(i).toString());
                                String nombre=jsonObject.getString("nombre_completo");
                                String descripcion=jsonObject.getString("descripcion");
                                String foto=jsonObject.getString("foto");
                                int id_tutor=jsonObject.getInt("id_tutor");
                                tutores.add(new TutoresFavoritos(nombre,descripcion,foto,id_tutor));

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

    public void buildItems(List<TutoresFavoritos> tutores){
        ListAdapter listAdapter= new ListAdapter(tutores, homepage.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TutoresFavoritos item) {
                moveToPerfilTutor(item);
            }
        });
        RecyclerView recyclerView= findViewById(R.id.lista_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(homepage.this));
        recyclerView.setAdapter(listAdapter);
    }
    public void moveToPerfilTutor(TutoresFavoritos item){
        Intent intent= new Intent(homepage.this,ver_datos_tutor.class);
        intent.putExtra("id_tutor",String.valueOf(item.getId()));
        startActivity(intent);
    }






/*
    private void createItemTutores(int n) {
        List<TutoresFavoritos> lista_tutores = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lista_tutores.add(new TutoresFavoritos("Lana Rohades", "Es habilidosa", "https://tinyurl.com/4m6nytvh", 1));
        }

        ListAdapter listAdapter = new ListAdapter(lista_tutores, this,ListAdapter.OnItemClickListener(){

        });
        RecyclerView recyclerView = findViewById(R.id.lista_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }


    class TaskTutoresFavoritos extends AsyncTask<Void, Void, Void> {
        private String stringConnection = "jdbc:mysql://buscatutordatabase.cuxsffuy95k9.us-east-1.rds.amazonaws.com:3306/buscatutor?UseUnicode=true&characterEncoding=utf8";
        private String user = "admin";
        private String password = "admin12345678";
        private String error = null;
        private int id = 0;
        private String nombre_usuario=null;
        private String foto_usuario="https://tinyurl.com/yc582czb";

        public void setId(int id) {
            this.id = id;
        }

        private List<TutoresFavoritos> tutoresFavoritos = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(stringConnection, user, password);
                Statement statement = connection.createStatement();
                //Cargamos los datos
                String queryTutoresFavoritos = "SELECT T.id_tutor, E.nombre_completo,T.descripcion, T.foto FROM Tutor as T\n" +
                        "\tLEFT JOIN Estudiante as E ON T.id_estudiante=E.id_estudiante\n" +
                        "    WHERE T.id_tutor IN (\n" +
                        "\t\t\t\t\t\tSELECT id_tutor FROM Favoritos\n" +
                        "\t\t\t\t\t\tWHERE id_estudiante=?\n" +
                        "\t\t\t\t\t\t);"; //se muestran los tutores favoritos
                String queryTutoresDisponibles = "SELECT T.id_tutor, E.nombre_completo,T.descripcion,T.foto FROM Tutor as T\n" +
                        "\tLEFT JOIN Estudiante as E ON T.id_estudiante=E.id_estudiante;"; //se muestran los tutores disponibles si es que el estudiante no tiene favoritos.

                PreparedStatement prepareStatement = connection.prepareStatement(queryTutoresFavoritos);
                prepareStatement.setInt(1, this.id);
                ResultSet resultSet = prepareStatement.executeQuery();
                Boolean containsFavorite = false;
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String nombre = resultSet.getString(2);
                    String descripcion = resultSet.getString(3);
                    String foto = resultSet.getString(4);
                    tutoresFavoritos.add(new TutoresFavoritos(nombre, descripcion, foto, id));
                    containsFavorite = true;
                }
                //si no tiene tutores favoritos, consultamos los tutores disponibles
                if (!containsFavorite) {
                    System.out.println("BUSCANDO TODOS LOS TUTORES DISPONIBLES");
                    prepareStatement = connection.prepareStatement(queryTutoresDisponibles);
                    resultSet = prepareStatement.executeQuery();
                    while (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        String nombre = resultSet.getString(2);
                        String descripcion = resultSet.getString(3);
                        String foto = resultSet.getString(4);
                        tutoresFavoritos.add(new TutoresFavoritos(nombre, descripcion, foto, id));
                        System.out.println("AGREGANDO TUTOR");
                    }
                    System.out.println("ESCANEO DE TUTORES COMPLETO");
                }
                //Por ultimo consultamos los datos del perfil
                System.out.println("CONSULTA DE DATOS DEL PERFIL DE USUARIO");
                prepareStatement=connection.prepareStatement("SELECT nombre_completo FROM Estudiante WHERE id_estudiante=?");
                prepareStatement.setInt(1,this.id);
                resultSet=prepareStatement.executeQuery();
                while(resultSet.next()){
                    this.nombre_usuario=resultSet.getString(1);
                }
                resultSet.close();
                connection.close();
                System.out.println("CONEXION A LA BASE DE DATOS CERRADA");

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                error = e.toString();
            }
            return null;
        }

        public List<TutoresFavoritos> getListaTutores() {
            return tutoresFavoritos;
        }

        @Override
        //Despues de cargar los datos
        protected void onPostExecute(Void aVoid) {
            System.out.println("OBTENIENDO LOS DATOS DEL PERFIL DE USUARIO");
            TextView textNombre=findViewById(R.id.id_nombre_usuario);
            ImageView image=findViewById(R.id.id_foto_usuario);

            textNombre.setText(this.nombre_usuario);
            Picasso.get()
                    .load(this.foto_usuario)
                    .error(R.mipmap.ic_launcher_round)
                    .into(image);

            System.out.println("EJECUTANDO EL RECYCLER VIEW");
            ListAdapter listAdapter= new ListAdapter(this.tutoresFavoritos, homepage.this, new ListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(TutoresFavoritos item) {
                    moveToPerfilTutor(item);
                }
            });
            RecyclerView recyclerView= findViewById(R.id.lista_items);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(homepage.this));
            recyclerView.setAdapter(listAdapter);
            super.onPostExecute(aVoid);
        }
        public void moveToPerfilTutor(TutoresFavoritos item){
            Intent intent= new Intent(homepage.this,ver_datos_tutor.class);
            intent.putExtra("id_tutor",String.valueOf(item.getId()));
            startActivity(intent);
        }

        public TaskTutoresFavoritos(int id) {
            this.id = id;
        }
    }

 */


}