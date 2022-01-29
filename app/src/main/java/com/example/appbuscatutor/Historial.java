package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class Historial extends AppCompatActivity {

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        drawerLayout =findViewById(R.id.drawer_layout);
    }
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
        homepage_tutores.redirectActivity(this,registrar_datos_tutor.class);
    }
    public void ClickBuscarTutor(View view){
        homepage_tutores.redirectActivity(this,buscar_tutor.class);
    }
    public void ClickHistorial(View view){
        recreate();

    }
    public void ClickCerrarSesion(View view){
        homepage_tutores.logout(this);
    }
    protected void onPause(){
        super.onPause();
        homepage_tutores.closeDrawer(drawerLayout);
    }
}