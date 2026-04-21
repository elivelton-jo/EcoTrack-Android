package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color; // IMPORT QUE ESTAVA FALTANDO
import android.graphics.Typeface;
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

        // Verifica LGPD
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        if (!pref.getBoolean("aceitou", false)) {
            startActivity(new Intent(this, LgpdActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista() {
        List<Recurso> lista = db.listarTodos();
        double totalKwh = 0;
        double totalLitros = 0;

        if (lista != null) {
            for (Recurso r : lista) {
                if (r instanceof Energia) {
                    totalKwh += r.getValor();
                } else if (r instanceof Agua) {
                    // Converte para litros para o dashboard
                    totalLitros += (r.getValor() * 1000);
                }
            }
        }

        // DESIGN PROFISSIONAL DO DASHBOARD
        String resumo = String.format(Locale.getDefault(),
                "CONSUMO TOTAL\n%.1f kWh  |  %.0f Litros", totalKwh, totalLitros);

        txtResumo.setText(resumo);
        txtResumo.setTextSize(22); // Fonte maior
        txtResumo.setTypeface(null, Typeface.BOLD); // Negrito
        txtResumo.setTextColor(Color.parseColor("#2E7D32")); // Verde escuro profissional
        txtResumo.setLineSpacing(0, 1.2f); // Espaçamento entre linhas

        // CONFIGURAÇÃO DO ADAPTER COM EXCLUSÃO
        RecursoAdapter adapter = new RecursoAdapter(lista, recurso -> {
            new AlertDialog.Builder(this)
                    .setTitle("Remover Registro")
                    .setMessage("Deseja excluir '" + recurso.getNome() + "'?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        db.deletar(recurso.getNome());
                        Toast.makeText(this, "Removido com sucesso!", Toast.LENGTH_SHORT).show();
                        atualizarLista(); // Recalcula na hora
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        recycler.setAdapter(adapter);
    }
}