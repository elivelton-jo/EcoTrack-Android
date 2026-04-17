package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private DbHelper db;
    private TextView txtResumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- LGPD: Verificação de Aceite ---
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        if (!pref.getBoolean("aceitou", false)) {
            startActivity(new Intent(this, LgpdActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Inicialização
        db = new DbHelper(this);
        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Botão para abrir tela de cadastro
        findViewById(R.id.btnNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));

        configurarSwipe();
    }

    /**
     * Configura o deslizar para a esquerda/direita para excluir um item.
     */
    private void configurarSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int dir) {
                int posicao = vh.getAdapterPosition();
                List<Recurso> listaAtual = db.listarTodos();
                Recurso recursoParaDeletar = listaAtual.get(posicao);

                // Deleta do banco de dados usando o ID
                db.deletar(recursoParaDeletar.getId());

                // Atualiza a lista e o dashboard
                atualizarLista();
                Toast.makeText(MainActivity.this, "Registro removido!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recycler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os dados sempre que voltar para esta tela
        atualizarLista();
    }

    /**
     * Atualiza o RecyclerView e recalcula o Dashboard de impacto (Erro 2).
     */
    private void atualizarLista() {
        List<Recurso> lista = db.listarTodos();

        double totalCo2 = 0;
        double totalAgua = 0;

        // Lógica do Dashboard: Percorre a lista e soma os impactos individuais
        for (Recurso r : lista) {
            if (r instanceof Energia) {
                // Cálculo específico de Energia (valor * 0.5 kg CO2)
                totalCo2 += (r.getValor() * 0.5);
            } else if (r instanceof Agua) {
                // Cálculo específico de Água (valor * 10 litros)
                totalAgua += (r.getValor() * 10);
            }
        }

        // Atualiza o texto do Dashboard na tela (Formatado com 1 casa decimal)
        String resumo = String.format(Locale.getDefault(),
                "Impacto: %.1f kg CO2 | %.0f L Água", totalCo2, totalAgua);
        txtResumo.setText(resumo);

        // Configura o Adapter passando a ação de clique (Erro 1)
        recycler.setAdapter(new RecursoAdapter(lista, recurso -> {
            // Ação ao clicar no item: Mostra detalhes em um Toast
            String detalhes = "Nome: " + recurso.getNome() +
                    "\nData: " + recurso.getData() +
                    "\nResultado: " + recurso.calcularImpacto();

            Toast.makeText(this, detalhes, Toast.LENGTH_LONG).show();
        }));
    }
}