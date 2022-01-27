package com.example.appbuscatutor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class registrar_datos_tutor extends AppCompatActivity {

    String ruta_foto;

    EditText edtEspecialidad, edtDescripcion, edtHabilidades, edtFoto;
    Button btnAgrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_tutor);

        edtEspecialidad = (EditText)findViewById(R.id.input_especialidad);
        edtDescripcion = (EditText)findViewById(R.id.input_descripcion);
        edtHabilidades = (EditText)findViewById(R.id.input_habilidades);
        edtFoto = (EditText)findViewById(R.id.input_especialidad);

        btnAgrega = (Button) findViewById(R.id.id_btncontinuar);

        btnAgrega.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //ejecutarServicio("https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/registro-tutor");
                ejecutarServicio("https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/registro-tutor?id_estudiante=96&descripcion=" +
                        edtDescripcion.getText().toString() +"&foto=https://tinyurl.com/397pywh4&habilidades=" +
                        edtHabilidades.getText().toString() + "&especialidades=" +
                        edtEspecialidad.getText().toString());
            }
        });

    }

    private void ejecutarServicio (String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String, String>();
                parametros.put("id_estudiante", "49");
                parametros.put("descripcion", edtDescripcion.getText().toString());
                parametros.put("foto", edtFoto.getText().toString());
                parametros.put("habilidades", edtHabilidades.getText().toString());
                parametros.put("especialidades", edtEspecialidad.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*boton para cargar la imagen*/
    public void btnSeleccionarImagen(View view) {
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione aplicación"),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            ruta_foto=path.toString();
            Toast.makeText(this,"Imagen seleccionada!",Toast.LENGTH_LONG).show();
        }
    }
    /*fin de cargar imagen - la direccion de la imagen esta en la variable "ruta_foto"*/
}