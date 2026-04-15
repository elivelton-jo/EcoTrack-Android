package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerRecursos;
    private RecursoAdapter adapter;
    private FloatingActionButton btnNovo;
    private LinearLayout layoutVazio;
    private CardView cardResumo;
    private TextView txtResumoImpacto;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- 1. PRIVACIDADE (LGPD) ---
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        if (!pref.getBoolean("aceitou_lgpd", false)) {
            startActivity(new Intent(this, LgpdActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // --- 2. INICIALIZAÇÃO ---
        db = new DbHelper(this);
        recyclerRecursos = findViewById(R.id.recyclerRecursos);
        btnNovo = findViewById(R.id.btnNovo);
        layoutVazio = findViewById(R.id.layoutVazio);
        cardResumo = findViewById(R.id.cardResumo);
        txtResumoImpacto = findViewById(R.id.txtResumoImpacto);

        recyclerRecursos.setLayoutManager(new LinearLayoutManager(this));

        // --- 3. EVENTOS ---
        btnNovo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CadastroActivity.class));
        });

        configurarSwipe();
    }

    private void configurarSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicao = viewHolder.getAdapterPosition();
                List<Recurso> listaAtual = db.listarTodos();
                Recurso recursoParaDeletar = listaAtual.get(posicao);

                db.deletarRecurso(recursoParaDeletar.getId());
                atualizarLista();

                Toast.makeText(MainActivity.this, "Registro removido!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerRecursos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista() {
        List<Recurso> listaDoBanco = db.listarTodos();

        if (listaDoBanco.isEmpty()) {
            layoutVazio.setVisibility(View.VISIBLE);
            recyclerRecursos.setVisibility(View.GONE);
            cardResumo.setVisibility(View.GONE);
        } else {
            layoutVazio.setVisibility(View.GONE);
            recyclerRecursos.setVisibility(View.VISIBLE);
            cardResumo.setVisibility(View.VISIBLE);

            atualizarDashboard(listaDoBanco);

            adapter = new RecursoAdapter(listaDoBanco);
            recyclerRecursos.setAdapter(adapter);
        }
    }

    /**
     * Lógica Sênior: Consolida os dados polimórficos em um resumo visual.
     */
    private void atualizarDashboard(List<Recurso> lista) {
        double totalCo2 = 0;
        double totalAgua = 0;

        for (Recurso r : lista) {
            // Verifica o tipo de objeto para somar o impacto específico
            if (r instanceof Energia) {
                // Exemplo: 0.5kg de CO2 por cada unidade de valor
                totalCo2 += (r.getValor() * 0.5);
            } else if (r instanceof Agua) {
                // Exemplo: 10 litros por cada unidade de valor
                totalAgua += (r.getValor() * 10);
            }
        }

        String resumo = String.format(Locale.getDefault(),
                "%.1f kg de CO2 e %.0fL de água", totalCo2, totalAgua);
        txtResumoImpacto.setText(resumo);
    }
}