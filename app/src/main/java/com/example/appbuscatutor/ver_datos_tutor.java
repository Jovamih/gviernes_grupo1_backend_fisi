package com.example.appbuscatutor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ver_datos_tutor extends AppCompatActivity {

    TextView nombre, especialidad1, especialidad2,especialidad3,descripcion, habilidades1,habilidades2,habilidades3;
    Button buttonContactar;
    ImageView foto;
    String numero, foto_text;
    String id_tutor=null;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_datos_tutor);
        //obtener el ID de la intefaz anterior
        Intent intent=getIntent();
        id_tutor=intent.getStringExtra("id_tutor");
        System.out.println("ID RECUPERADO DE HOMEPAGE= "+id_tutor);

        nombre=(TextView)findViewById(R.id.textView_nombre);
        especialidad1=(TextView)findViewById(R.id.textView_especialidad1);
        especialidad2=(TextView)findViewById(R.id.textView_especialidad2);
        especialidad3=(TextView)findViewById(R.id.textView_especialidad3);
        descripcion=(TextView)findViewById(R.id.textView_descripcion);
        habilidades1=(TextView)findViewById(R.id.textView_habilidades1);
        habilidades2=(TextView)findViewById(R.id.textView_habilidades2);
        habilidades3=(TextView)findViewById(R.id.textView_habilidades3);
        buttonContactar=(Button)findViewById(R.id.buttonContactar);
        foto=(ImageView)findViewById(R.id.id_foto);
        //new ver_datos_tutor.Task().execute();

        queue = Volley.newRequestQueue(this);
        MetodoGet();

        //cuando damos click en boton contactar
        buttonContactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = isAppInstalled("com.whatsapp");
                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+51"+numero+"&text=¡Hola tutor! Me gustaría ponerme en contacto con usted."));
                    startActivity(intent);
                }else{
                    Toast.makeText(ver_datos_tutor.this,"Whatsapp no instalado!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //*fin de boton contactar
    }

    //click en boton
    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;
        try{
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed=true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed=false;
            e.printStackTrace();
        }
        return is_installed;
    }

    //*************************************************
    private void MetodoGet(){

        String endpoint="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/tutores?id="+id_tutor;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray mJsonArray = response.getJSONArray("tutores");
                    JSONObject mJsonObject = mJsonArray.getJSONObject(mJsonArray.length()-1);
                    nombre.setText(mJsonObject.getString("nombre_completo"));
                    numero=mJsonObject.getString("num_telefono");
                    foto_text=mJsonObject.getString("foto");
                    descripcion.setText(mJsonObject.getString("descripcion"));

                    /*ArrayList<Object> lista_especialidades = new ArrayList<Object>();
                    for (int i=0;i<mJsonObject.getJSONArray("especialidades").length();i++){
                        System.out.println(mJsonObject.getJSONArray("especialidades").get(i));
                        lista_especialidades.add(mJsonObject.getJSONArray("especialidades").getString(i));
                    }
                    System.out.println(lista_especialidades);*/
                    especialidad1.setText(mJsonObject.getJSONArray("especialidades").getString(0));
                    especialidad2.setText(mJsonObject.getJSONArray("especialidades").getString(1));
                    habilidades1.setText(mJsonObject.getJSONArray("habilidades").getString(0));
                    habilidades2.setText(mJsonObject.getJSONArray("habilidades").getString(1));

                    Picasso.get()
                            .load(foto_text)
                            .error(R.mipmap.ic_launcher_round)
                            .into(foto);
                    habilidades3.setText(mJsonObject.getJSONArray("habilidades").getString(2));
                    especialidad3.setText(mJsonObject.getJSONArray("especialidades").getString(2));

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