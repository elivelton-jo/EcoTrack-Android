package com.example.ecotrack.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.model.Recurso;
import java.util.List;
import android.graphics.Typeface;
import com.example.ecotrack.model.Energia;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {

    private List<Recurso> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemLongClick(Recurso recurso);
    }

    public RecursoAdapter(List<Recurso> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recurso r = lista.get(position);

        // No onBindViewHolder do RecursoAdapter
        holder.text1.setText(r.getNome().toUpperCase()); // Nome em Caps para parecer rótulo
        holder.text1.setTextSize(16);
        holder.text1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        holder.text1.setTextColor(Color.parseColor("#212121")); // Preto quase total

        holder.text2.setText("📅 " + r.getData() + "  |  ⚡ " + r.getValor() + (r instanceof Energia ? " kWh" : " L"));
        holder.text2.setTextSize(13);
        holder.text2.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        holder.text2.setTextColor(Color.parseColor("#757575")); // Cinza elegante

        // Lógica de clique longo para apagar
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(r);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}