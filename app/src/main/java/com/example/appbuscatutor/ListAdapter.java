package com.example.appbuscatutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    //Lista de elementos de los tutores favoritos
    private List<TutoresFavoritos> lista_tutores;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(TutoresFavoritos item);
    }

    public ListAdapter(List<TutoresFavoritos> lista_tutores, Context context,ListAdapter.OnItemClickListener listener){
        this.lista_tutores=lista_tutores;
        this.context=context;
        this.mInflater= LayoutInflater.from(context);
        this.mInflater= LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.recycler_template,null);

        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
            holder.bindData(lista_tutores.get(position));
    }

    @Override
    public int getItemCount() {
        return lista_tutores.size();
    }
    public void setItems(List<TutoresFavoritos> items){
        lista_tutores=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textNombre,textDescripcion;
        ImageView foto;

        ViewHolder(View itemView){
            super(itemView);
            foto=itemView.findViewById(R.id.id_foto_tutor);
            textNombre=itemView.findViewById(R.id.id_nombre_tutor);
            textDescripcion=itemView.findViewById(R.id.id_descripcion_tutor);
        }
        void bindData(final TutoresFavoritos item){
            Picasso.get()
                    .load(item.getFoto())
                    .error(R.mipmap.ic_launcher_round)
                    .into(foto);
            textNombre.setText(item.getNombre());
            textDescripcion.setText(item.getDescripcion());
            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }

    }
}
