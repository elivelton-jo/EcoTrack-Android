package com.example.ecotrack.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.model.Recurso;
import java.util.List;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {
    private List<Recurso> lista;
    private OnItemClickListener listener; // Interface para o clique

    // Interface para comunicar o clique com a Activity
    public interface OnItemClickListener {
        void onItemClick(Recurso recurso);
    }

    public RecursoAdapter(List<Recurso> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usando o layout padrão do Android para dois textos
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recurso r = lista.get(position);
        holder.t1.setText(r.getNome() + " (" + r.getData() + ")");
        holder.t2.setText(r.calcularImpacto());

        // Configura o clique no item da lista
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(r);
            }
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1, t2;
        public ViewHolder(@NonNull View v) {
            super(v);
            t1 = v.findViewById(android.R.id.text1);
            t2 = v.findViewById(android.R.id.text2);
        }
    }
}