package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class paypal extends AppCompatActivity {

    PayPalButton payPalButton;
    String id_estudiante = null;
    int id_est=0;


    //Varables requeridas para el envío del correo
    private String correoDestino = "alefran2020@gmail.com";
    private String api_correo = "https://api-dm-email.herokuapp.com/api/enviar/";
    private RequestQueue requestQueue;
    private JSONObject jsoncorreo;
    private String ENDPOINT_ESTUDIANTE="https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/estudiantes?id=%d";

    public static final String YOUR_CLIENT_ID = "AaE0sFL_clUJomQ0mZZUG35rBqw-MqzxP-0gPnZSqjDq_If3IpEVbKrvR4DP5qjlTYiNTBXF31XD6d4T";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestQueue= Volley.newRequestQueue(this);
        String descripcion = getIntent().getExtras().getString("descripcion");
        String habilidades = getIntent().getExtras().getString("habilidades");
        String especialidades = getIntent().getExtras().getString("especialidades");
        id_estudiante = getIntent().getExtras().getString("id_estudiante");
        id_est=Integer.parseInt(id_estudiante);
        get_correo_estudiante();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        payPalButton = findViewById(R.id.payPalButton);

        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay","com.example.appbuscatutor"),
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);

        payPalButton.setup(
                new CreateOrder() {

                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value("5.99")
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }

                }, new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                ejecutarServicio("https://825tzl1d6f.execute-api.us-east-1.amazonaws.com/v1/registro-tutor?id_estudiante="+
                                        id_estudiante+"&descripcion=" +
                                        descripcion +"&foto=https://tinyurl.com/397pywh4&habilidades=" +
                                        habilidades + "&especialidades=" +
                                        especialidades);


                                System.out.println("ID ESTUDIANTE PAYPAL: " + id_estudiante);

                                //Para el correo
                                System.out.println("CORREO: " + correoDestino);
                                enviarMail(api_correo + correoDestino);
                                Intent intent = new Intent(paypal.this, verificacion_paypal.class);
                                intent.putExtra("id_estudiante", id_estudiante);
                                startActivity(intent);
                            }
                        });
                    }
                },
                new OnCancel() {
                    @Override
                    public void onCancel() {
                        Log.d("OnCancel", "Buyer cancelled the PayPal experience.");
                        System.out.println("Cancelado");
                    }
                },
                new OnError() {
                    @Override
                    public void onError(@NotNull ErrorInfo errorInfo) {
                        Log.d("OnError", String.format("Error: %s", errorInfo));

                    }
                }
        );

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
                return null;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void enviarMail (String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Notificación del pago enviado a su correo", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void get_correo_estudiante(){
        ENDPOINT_ESTUDIANTE=String.format(ENDPOINT_ESTUDIANTE,id_est);
        JsonObjectRequest estudiante_request= new JsonObjectRequest(Request.Method.GET, ENDPOINT_ESTUDIANTE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean success= response.getBoolean("success");
                    if(success){
                        // Si el perfil del estudiante existe
                        JSONObject data= response.getJSONObject("data");
                        correoDestino = data.getString("correo");
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

}