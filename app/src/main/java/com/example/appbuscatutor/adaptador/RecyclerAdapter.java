package com.example.appbuscatutor.adaptador;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appbuscatutor.R;
//import com.example.appbuscatutor.DetailActivity
import com.example.appbuscatutor.ver_datos_tutor;
import com.example.appbuscatutor.Tutor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private List<Tutor> items;
    private List<Tutor> originalItems;
    private RecyclerItemClick itemClick;

    private LayoutInflater mInflater;
    private Context context;
    final RecyclerAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Tutor item);
    }

    /*public RecyclerAdapter(List<Tutor> items, Context context, RecyclerItemClick itemClick) {
        this.items = items;
        this.itemClick = itemClick;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(items);
    }*/

    public RecyclerAdapter(List<Tutor> lista_tutores, Context context, RecyclerAdapter.OnItemClickListener listener){
        this.items=lista_tutores;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(items);
        this.context=context;
        this.mInflater= LayoutInflater.from(context);
        this.listener=listener;
    }

    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_tutor, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, final int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filter(final String strSearch) {
        if (strSearch.length() == 0) {
            items.clear();
            items.addAll(originalItems);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                items.clear();
                List<Tutor> collect = originalItems.stream()
                        .filter(i -> i.getEspecialidades().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());

                items.addAll(collect);
            }
            else {
                items.clear();
                for (Tutor i : originalItems) {
                    if (i.getNombre().toLowerCase().contains(strSearch)) {
                        items.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvNombre;
        private TextView tvDescripcion;

        public RecyclerHolder(@NonNull View itemView_1) {
            super(itemView_1);

            imgItem = itemView.findViewById(R.id.imgTutor);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }

        void bindData(final Tutor item){
            Picasso.get()
                    .load(item.getImgResource())
                    .error(R.mipmap.ic_launcher_round)
                    .into(imgItem);
            tvNombre.setText(item.getNombre());
            tvDescripcion.setText(item.getDescripcion());

            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }
    }

    public interface RecyclerItemClick {
        void itemClick(Tutor item);
    }
}