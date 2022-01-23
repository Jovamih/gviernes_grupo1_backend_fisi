package com.example.appbuscatutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.appbuscatutor.adaptador.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class buscar_tutor extends AppCompatActivity implements RecyclerAdapter.RecyclerItemClick, SearchView.OnQueryTextListener {

    private RecyclerView rvLista;
    private SearchView svSearch;
    private RecyclerAdapter adapter;
    private List<Tutor> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_tutor);
        initViews();
        initValues();
        initListener();
    }

    private void initViews(){
        rvLista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);
    }

    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);

        items = getItems();
        adapter = new RecyclerAdapter(items, this);
        rvLista.setAdapter(adapter);
    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }

    private List<Tutor> getItems() {
        List<Tutor> itemLists = new ArrayList<>();
        itemLists.add(new Tutor("Brian Maza", "Especialista en matematicas", R.drawable.profesor,2));
        itemLists.add(new Tutor("Alan Jerez", "Profesor de historia", R.drawable.profesor,3));
        itemLists.add(new Tutor("Laura Ali", "Didactico", R.drawable.profesor,2));
        itemLists.add(new Tutor("Mara Salvador", "Puntual y responsable", R.drawable.profesor,2));
        itemLists.add(new Tutor("Elias Fernandez", "Especialista en ciencias politicas", R.drawable.profesor,2));
        itemLists.add(new Tutor("Carlos San Jose", "Amante de la literatura", R.drawable.profesor,2));
        itemLists.add(new Tutor("Iago Pardo", "Deportista y buen ciudadano", R.drawable.profesor,2));
        itemLists.add(new Tutor("Olivia Moro", "Especialista en linguistica", R.drawable.profesor,2));
        itemLists.add(new Tutor("Juan Enrique Cantos", "Profesor adaptado a la actualidad", R.drawable.profesor,2));
        itemLists.add(new Tutor("Juan Pablo Soriano", "Promoviendo nuevos valores", R.drawable.profesor,2));
        itemLists.add(new Tutor("Valeria Ventura", "Construyendo profesionales", R.drawable.profesor,2));
        itemLists.add(new Tutor("Dunia Hernández", "Peru por siempre", R.drawable.profesor,2));
        return itemLists;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    @Override
    public void itemClick(Tutor item) {
        Intent intent = new Intent(this, ver_datos_tutor.class);
        intent.putExtra("itemDetail", item);
        startActivity(intent);
    }
}