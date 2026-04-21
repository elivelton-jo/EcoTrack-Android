package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrack.R;
import com.example.ecotrack.view.RecursoAdapter;
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

        // 1. VERIFICAÇÃO DE LGPD (Deve ser a primeira coisa no onCreate)
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        boolean jaAceitou = pref.getBoolean("aceitou", false);

        if (!jaAceitou) {
            startActivity(new Intent(this, LgpdActivity.class));
            finish(); // Fecha a Main para obrigar a passar pela LGPD
            return;
        }

        setContentView(R.layout.activity_main);

        // 2. INICIALIZAÇÃO
        db = new DbHelper(this);
        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Botão Novo Registro
        findViewById(R.id.btnNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista(); // Atualiza sempre que volta para esta tela
    }

    private void atualizarLista() {
        List<Recurso> lista = db.listarTodos();

        double totalKwh = 0;
        double totalLitros = 0;

        // 3. CÁLCULO DO DASHBOARD
        if (lista != null) {
            for (Recurso r : lista) {
                if (r instanceof Energia) {
                    totalKwh += r.getValor();
                } else if (r instanceof Agua) {
                    totalLitros += (r.getValor() * 1000); // Converte m3 para Litros
                }
            }
        }

        // Atualiza o texto do topo (Dashboard)
        String resumo = String.format(Locale.getDefault(),
                "Total: %.1f kWh | %.0f L Água", totalKwh, totalLitros);
        txtResumo.setText(resumo);

        // 4. CONFIGURAÇÃO DO ADAPTER COM EXCLUSÃO (CLIQUE LONGO)
        RecursoAdapter adapter = new RecursoAdapter(lista, recurso -> {
            // Criar Alerta de Confirmação para deletar
            new AlertDialog.Builder(this)
                    .setTitle("Remover Registro")
                    .setMessage("Deseja excluir '" + recurso.getNome() + "'?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        db.deletar(recurso.getNome()); // Chama o delete no banco
                        Toast.makeText(this, "Removido!", Toast.LENGTH_SHORT).show();
                        atualizarLista(); // RECALCULA TUDO NA HORA
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        recycler.setAdapter(adapter);
    }
}