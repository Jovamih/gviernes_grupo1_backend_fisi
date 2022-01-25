package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class registrar_usuario extends AppCompatActivity {

    private String ENDPOINT_REGISTRAR_USUARIO="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/registro-estudiante";
    private String correo,password,nombre_completo,num_telefono;
    private RequestQueue requestQueue;
    private JSONObject responseUsuario;
    private int id_estudiante=0;
    Toast toast=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        Button button= (Button)findViewById(R.id.id_btn_registrar);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nombre_completo=((EditText)findViewById(R.id.id_text_nombres)).getText().toString().trim();
                num_telefono=((EditText)findViewById(R.id.id_text_numero)).getText().toString().trim();
                correo= ((EditText)findViewById(R.id.id_text_email)).getText().toString().trim();
                password= ((EditText)findViewById(R.id.id_text_password)).getText().toString().trim();
                String password_verify=((EditText)findViewById(R.id.id_text_password_verify)).getText().toString().trim();
                System.out.println("DATOS");
                System.out.println("NOMBRE="+nombre_completo);
                System.out.println("TELEFONO="+num_telefono);
                System.out.println("CORREO="+correo);
                System.out.println("password="+password);
                System.out.println("Password verify="+password_verify);


                //primera verificacion de password coincidentes
                if(password.equals(password_verify)) {//si  coincide iniciamos el proceso de insercion
                    toast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);//
                    System.out.println("Las contraseñas son iguales");
                    //mostramos la pagina de carga
                    ProgressDialog dialog=ProgressDialog.show(registrar_usuario.this,"","Registrando",true);
                    RegistrarUsuarioResponse(new VolleyCallback() {
                        @Override
                        public void onSuccess() throws JSONException {
                            dialog.dismiss();
                            switch (responseUsuario.getInt("status")){
                                case 1:
                                    Toast.makeText(getApplicationContext(), "Usuario registrado existosamente!", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(registrar_usuario.this,homepage_tutores.class);
                                    intent.putExtra("id_estudiante",String.valueOf(id_estudiante));
                                    //antes de pasarnos a la actividad HOME, cerramos el dialog

                                    startActivity(intent);
                                    break;
                                case 3:
                                    Toast.makeText(getApplicationContext(), "El correo ya esta en uso.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }
                    });
                    //retiramos el dialog
                    //dialog.dismiss();
                } else{//en caso contrario procedemos con la insercion en la base de datos
                    // click handling code
                    System.out.println("Las contraseñas no  conciden");
                    //Looper.prepare();
                    toast=Toast.makeText(getBaseContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT);
                    toast.show();

                }


            }
        });
    }

    public void RegistrarUsuarioResponse(final VolleyCallback callback ){
        System.out.println("Accediendo al ENDPOINT: "+ENDPOINT_REGISTRAR_USUARIO);
        //configuraciones  de parametros
        Map<String,String> params= new HashMap<>();
        params.put("correo",this.correo);
        params.put("password",this.password);
        params.put("nombre_completo",this.nombre_completo);
        params.put("num_telefono",this.num_telefono);
        params.put("es_tutor","0");


        JSONObject parameters=new JSONObject(params);
        //Iniciamos la pantalla de carga

        String URL_PORT=String.format("https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/registro-estudiante?correo=%s&password=%s&nombre_completo=%s&num_telefono=%s&es_tutor=%d",this.correo,this.password,this.nombre_completo,this.num_telefono,0);

        //JSON Request
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                URL_PORT,
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("KEY RESPONSE="+response.keys());
                try {
                    int status=response.getInt("status");
                    String message=response.getString("message");
                    //int id_estudiante =response.getInt("id_estudiante")
                    if(status==1){
                        id_estudiante=response.getInt("id_estudiante");
                    }
                    responseUsuario= response;
                    callback.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error encontrado: response==|"+error);
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers= new HashMap<>();
                headers.put("Content-Type","Application/json");
                return headers;
            }
        };
        System.out.println("REQUEST= "+jsonRequest.getUrl());
        System.out.println("BODY="+jsonRequest.getBody());
        System.out.println("PARAMS="+jsonRequest.toString());
        System.out.println("URL MODIFED="+URL_PORT);
        requestQueue.add(jsonRequest);


    }
}

/*
class RegistrarUsuariosDatabase extends AsyncTask<Void,Void,Void> {
    private String stringConnection = "jdbc:mysql://buscatutordatabase.cuxsffuy95k9.us-east-1.rds.amazonaws.com:3306/buscatutor?UseUnicode=true&characterEncoding=utf8";
    private String user = "admin";
    private String password = "admin12345678";
    private String error = null;
    private int id = 0;
    private String nombre_usuario=null;
    private String foto_usuario="https://tinyurl.com/yc582czb";
    //Datos personales
    private String correo_usuario=null;
    private String password_usuario=null;
    private String nombre_completo=null;
    private String num_telefono=null;
    private ProgressDialog dialog=null;
    private EventoRegistro event=EventoRegistro.ERROR_REGISTRO;

    public RegistrarUsuariosDatabase(String correo,String password,String nombre_usuario,String num_telefono){
        this.correo_usuario=correo;
        this.password_usuario=password;
        this.nombre_completo=nombre_usuario;
        this.num_telefono=num_telefono;
        //iniciamos la ventana de cargando
        this.dialog=ProgressDialog.show(registrar_usuario.this,"","Registrando usuario",true);

    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection= DriverManager.getConnection(stringConnection,user,password);
            Statement statement =connection.createStatement();
            //creamos las consultas de verificacion de correo electronico ya existente
            String queryEmail="SELECT * FROM Estudiante WHERE correo=?";
            //creamos las consultas para insertar datos en la tabla de usuarios
            String queryInsertdata="INSERT INTO Estudiante(correo,password,nombre_completo,num_telefono,es_tutor) VALUES (?,?,?,?,?)";
            //creamos las consultas para consultar el ID del estudiante registrado (aprovachamos la facultad de ser auto-incremental.
            String queryLastID="SELECT MAX(id_estudiante) FROM Estudiante";

            //comenzamos a realizar la primera consulta
            PreparedStatement preparedStatement=connection.prepareStatement(queryEmail);
            preparedStatement.setString(1,this.correo_usuario);

            ResultSet resultSet=preparedStatement.executeQuery();
            //Si existe un correo igual, lanzo un mensaje
            if(resultSet.next()){
                dialog.dismiss();
                event=EventoRegistro.CORREO_EXISTENTE;
            }else{
                //si no existe un correo igual, continuamos con el registro de base de datos.
                preparedStatement=connection.prepareStatement(queryInsertdata);
                preparedStatement.setString(1,this.correo_usuario);
                preparedStatement.setString(2,this.password_usuario);
                preparedStatement.setString(3,this.nombre_completo);
                preparedStatement.setString(4,this.num_telefono);
                preparedStatement.setInt(5,0);//con 0 indicamos que no es tutor por defecto
                //ejecutamos la insercion
                int rowcount=preparedStatement.executeUpdate();
                if(rowcount>0){

                    //Toast.makeText(registrar_usuario.this,"Registro de datos exitoso!",Toast.LENGTH_SHORT);
                    //Comenzamos los preparativos para obtener el ID del usuario estudiante creado.
                    preparedStatement=connection.prepareStatement(queryLastID);
                    resultSet=preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        this.id = resultSet.getInt(1); //obtengo el ID
                    }
                    dialog.dismiss();
                    event=EventoRegistro.REGISTRO_EXITOSO;


                }else{
                    dialog.dismiss();
                    event=EventoRegistro.ERROR_REGISTRO;

                }
            }
            //si hubo algun error, la pantalla de dialogo se cierra
            //dialog.dismiss();
            preparedStatement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            dialog.dismiss();
            event=EventoRegistro.ERROR_REGISTRO;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void res){
        dialog.dismiss();
        switch (this.event){
            case CORREO_EXISTENTE:

                SingleToast.INSTANCE.show(registrar_usuario.this, "El correo ya existe", Toast.LENGTH_SHORT);
                break;
            case ERROR_REGISTRO:
                SingleToast.INSTANCE.show(registrar_usuario.this,"ERROR AL REGISTRAR USUARIO",Toast.LENGTH_SHORT);
                break;
            case REGISTRO_EXITOSO:
                //envio el ID a la actividad de HomePage tutores favoritos
                Intent intent=new Intent(registrar_usuario.this,homepage_tutores.class);
                intent.putExtra("id_usuario",String.valueOf(this.id));
                //antes de pasarnos a la actividad HOME, cerramos el dialog

                startActivity(intent);
                break;
            default:
                break;
        }

        super.onPostExecute(res);

    }
}


    enum EventoRegistro{
            ERROR_REGISTRO,
            CORREO_EXISTENTE,
            REGISTRO_EXITOSO,
           EXCEPCION
    };
    public enum SingleToast {
        INSTANCE;

        private Toast currentToast;
        private String currentMessage;

        public void show(Context context, String message, int duration) {
            if (message.equals(currentMessage)) {
                currentToast.cancel();
            }
            currentToast = Toast.makeText(context, message, duration);
            currentToast.show();

            currentMessage = message;
        }
    }

*/

