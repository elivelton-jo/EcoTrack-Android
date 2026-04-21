package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
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

        // 1. Verificação de LGPD
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        if (!pref.getBoolean("aceitou", false)) {
            startActivity(new Intent(this, LgpdActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // 2. Inicialização dos componentes
        db = new DbHelper(this);
        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // 3. Botão para novo cadastro
        findViewById(R.id.btnNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza sempre que o usuário volta para esta tela
        atualizarLista();
    }

    private void atualizarLista() {
        List<Recurso> lista = db.listarTodos();

        double totalKwh = 0;
        double totalLitros = 0;

        if (lista != null) {
            for (Recurso r : lista) {
                if (r instanceof Energia) {
                    totalKwh += r.getValor(); // Soma kWh
                } else if (r instanceof Agua) {
                    // Se o usuário digita em m3, multiplicamos por 1000 para Litros
                    // Se ele já digita em Litros, basta somar r.getValor()
                    totalLitros += (r.getValor() * 1000);
                }
            }
        }

        // --- ATUALIZAÇÃO DO DASHBOARD (Litros em vez de m3) ---
        String textoDashboard = String.format(Locale.getDefault(),
                "Total: %.1f kWh | %.0f Litros Água", totalKwh, totalLitros);

        txtResumo.setText(textoDashboard);

        // 7. Configura o Adapter
        RecursoAdapter adapter = new RecursoAdapter(lista, recurso -> {
            Toast.makeText(this, recurso.calcularImpacto(), Toast.LENGTH_SHORT).show();
        });
        recycler.setAdapter(adapter);


    }
}