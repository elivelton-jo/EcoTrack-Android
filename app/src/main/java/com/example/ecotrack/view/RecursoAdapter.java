package com.example.ecotrack.view; // Verifique se sua pasta é exatamente esta

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// ESTES IMPORTS SÃO OS MAIS IMPORTANTES:
import com.example.ecotrack.R;
import com.example.ecotrack.model.Recurso; // Se sua classe de dados tiver outro nome, mude aqui

import java.util.List;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder> {

    private List<Recurso> listaRecursos;

    public RecursoAdapter(List<Recurso> lista) {
        this.listaRecursos = lista;
    }
    public void setLista(List<Recurso> lista) {
        this.listaRecursos = lista;
    }

    @NonNull
    @Override
    public RecursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recurso, parent, false);
        return new RecursoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecursoViewHolder holder, int position) {
        Recurso recurso = listaRecursos.get(position);

        // Seta o Nome do recurso (ex: Conta de Luz)
        holder.text1.setText(recurso.getNome());

        // Seta a Data e o Valor no lugar da descrição que não existia
        String detalhes = "Data: " + recurso.getData() + " | Valor: R$ " + recurso.getValor();
        holder.text2.setText(detalhes);

        // ANIMAÇÃO DE MOVIMENTO (PONTO 4)
        holder.itemView.setAlpha(0f);
        holder.itemView.setTranslationY(50f);
        holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(position * 30)
                .start();
        // CLIQUE LONGO PARA DELETAR
        holder.itemView.setOnLongClickListener(v -> {
            // Criar um alerta de confirmação
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Deletar Registro")
                    .setMessage("Deseja realmente excluir " + recurso.getNome() + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // Chama o banco para deletar
                        com.example.ecotrack.database.DbHelper db = new com.example.ecotrack.database.DbHelper(v.getContext());
                        db.deletar(recurso.getNome());

                        // Remove da lista visual e avisa o Adapter
                        listaRecursos.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listaRecursos.size());

                        // Dica: Para atualizar o resumo no topo da tela,
                        // o ideal é recarregar a MainActivity ou usar um Callback.
                        android.widget.Toast.makeText(v.getContext(), "Excluído!", android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
    }
    @Override
    public int getItemCount() {
        return listaRecursos != null ? listaRecursos.size() : 0;
    }

    public static class RecursoViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public RecursoViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }

}