package com.example.ecotrack.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.model.Recurso;
import java.util.List;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder> {

    private List<Recurso> listaRecursos;

    public RecursoAdapter(List<Recurso> listaRecursos) {
        this.listaRecursos = listaRecursos;
    }

    @NonNull
    @Override
    public RecursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item que estilizamos com CardView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recurso, parent, false);
        return new RecursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecursoViewHolder holder, int position) {
        Recurso recurso = listaRecursos.get(position);

        // --- TRATAMENTO DO NOME (Iniciais Maiúsculas) ---
        String nomeOriginal = recurso.getNome();
        if (nomeOriginal != null && !nomeOriginal.isEmpty()) {
            // Transforma "agua" em "Agua" ou "conta de luz" em "Conta de luz"
            String nomeFormatado = nomeOriginal.substring(0, 1).toUpperCase() + nomeOriginal.substring(1).toLowerCase();
            holder.txtNome.setText(nomeFormatado);
        } else {
            holder.txtNome.setText("Sem nome");
        }

        // --- DATA ---
        holder.txtData.setText(recurso.getData());

        // --- IMPACTO (POLIMORFISMO) ---
        // Aqui o Java decide se chama o calcularImpacto de Agua ou Energia automaticamente
        holder.txtImpacto.setText(recurso.calcularImpacto());
    }

    @Override
    public int getItemCount() {
        return listaRecursos.size();
    }

    // ViewHolder que mapeia os componentes do XML item_recurso.xml
    public static class RecursoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtData, txtImpacto;

        public RecursoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeItem);
            txtData = itemView.findViewById(R.id.txtDataItem);
            txtImpacto = itemView.findViewById(R.id.txtImpactoItem);
        }
    }
}