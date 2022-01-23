package com.example.appbuscatutor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import android.os.Bundle;

public class ver_datos_tutor extends AppCompatActivity {

    TextView nombre, especialidad, descripcion, habilidades;
    Button buttonContactar;
    ImageView foto;
    String numero;
    String id_tutor=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_datos_tutor);
        //obtener el ID de la intefaz anterior
        Intent intent=getIntent();
        id_tutor=intent.getStringExtra("id_tutor");
        System.out.println("ID RECUPERADO DE HOMEPAGE= "+id_tutor);

        nombre=(TextView)findViewById(R.id.textView_nombre);
        especialidad=(TextView)findViewById(R.id.textView_especialidad);
        descripcion=(TextView)findViewById(R.id.textView_descripcion);
        habilidades=(TextView)findViewById(R.id.textView_habilidades);
        buttonContactar=(Button)findViewById(R.id.buttonContactar);
        foto=(ImageView)findViewById(R.id.id_foto);
        new ver_datos_tutor.Task().execute();

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


    //******************************CONEXION A LA BASE DE DATOS*************************/
    class Task extends AsyncTask<Void, Void, Void> {
        String records_nombre = "",records_especialidad = "", records_descripcion = "",records_habilidades = "",records_foto = "",error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://buscatutordatabase.cuxsffuy95k9.us-east-1.rds.amazonaws.com:3306/buscatutor?UseUnicode=true&characterEncoding=utf8", "admin", "admin12345678");
                Statement statement = connection.createStatement();

                /*Cargamos los datos*/
                //PreparedStatement sql_cargar = connection.prepareStatement("SELECT * FROM test WHERE id_test=?");
                PreparedStatement sql_cargar = connection.prepareStatement("SELECT T.id_tutor, E.nombre_completo,  ET.desc_especialidad, T.descripcion, HT.desc_habilidad, E.num_telefono, T.foto FROM Tutor as T\n" +
                                                                                                                                                    "\tLEFT JOIN Estudiante as E ON T.id_estudiante=E.id_estudiante\n" +
                                                                                                                                                    "    LEFT JOIN EspecialidadesTutor as ET  ON T.id_tutor=ET.id_tutor\n" +
                                                                                                                                                    "    LEFT JOIN HabilidadesTutor as HT ON T.id_tutor=HT.id_tutor\n" +
                                                                                                                                                    "    WHERE T.id_tutor=?\n" +
                                                                                                                                                    "    LIMIT 1\n" +
                                                                                                                                                    "    ;");
                sql_cargar.setString(1,id_tutor);   //setInt, setDouble
                ResultSet resultSet = sql_cargar.executeQuery();
                while (resultSet.next()) {
                    records_nombre = resultSet.getString(2) ;
                    records_especialidad = resultSet.getString(3) ;
                    records_descripcion = resultSet.getString(4) ;
                    records_habilidades = resultSet.getString(5) ;
                    numero = resultSet.getString(6) ;
                    records_foto = resultSet.getString(7) ;
                }
                resultSet.close();
                /* fin de cargar datos */

            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }
        @Override
        //Despues de cargar los datos
        protected void onPostExecute(Void aVoid) {
            nombre.setText(records_nombre);
            especialidad.setText(records_especialidad);
            descripcion.setText(records_descripcion);
            habilidades.setText(records_habilidades);
            Picasso.get()
                    .load(records_foto)
                    .error(R.mipmap.ic_launcher_round)
                    .into(foto);
            if(error != "")
                //b.setText(error);
                super.onPostExecute(aVoid);
        }
    }
}