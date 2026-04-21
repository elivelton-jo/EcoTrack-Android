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

        // CORRIGINDO O ERRO DE CORES (Texto Preto para visibilidade)
        holder.text1.setText(r.getNome() + " (" + r.getData() + ")");
        holder.text1.setTextColor(Color.BLACK);

        holder.text2.setText("Valor: " + r.getValor() + " | Impacto: " + r.calcularImpacto());
        holder.text2.setTextColor(Color.DKGRAY);

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