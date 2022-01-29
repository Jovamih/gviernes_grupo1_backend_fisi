package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;



public class registrar_datos_tutor extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String ruta_foto;
    String id_estudiante = null;
    EditText edtEspecialidad, edtDescripcion, edtHabilidades, edtFoto;
    Button btnAgrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_tutor);
        drawerLayout =findViewById(R.id.drawer_layout);
        Intent intent=getIntent();
        id_estudiante=intent.getStringExtra("id_estudiante");
        edtEspecialidad = (EditText)findViewById(R.id.input_especialidad);
        edtDescripcion = (EditText)findViewById(R.id.input_descripcion);
        edtHabilidades = (EditText)findViewById(R.id.input_habilidades);
        edtFoto = (EditText)findViewById(R.id.input_especialidad);

        btnAgrega = (Button) findViewById(R.id.id_btncontinuar);

        btnAgrega.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String desc =new String(edtDescripcion.getText().toString());
                String hab =new String(edtHabilidades.getText().toString());
                String esp =new String(edtEspecialidad.getText().toString());
                Intent intent = new Intent(getApplicationContext(), paypal.class);
                intent.putExtra("descripcion", desc);
                intent.putExtra("habilidades", hab);
                intent.putExtra("especialidades", esp);
                intent.putExtra("id_estudiante", id_estudiante);
                startActivity(intent);
            }
        });
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
        recreate();
    }
    public void ClickBuscarTutor(View view){
        homepage_tutores.redirectActivity(this,buscar_tutor.class);
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