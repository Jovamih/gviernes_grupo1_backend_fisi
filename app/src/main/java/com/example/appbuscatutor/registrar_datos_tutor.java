package com.example.appbuscatutor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class registrar_datos_tutor extends AppCompatActivity {

    String ruta_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_tutor);

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