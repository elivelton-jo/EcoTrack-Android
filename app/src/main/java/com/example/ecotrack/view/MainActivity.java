package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private DbHelper db;
    private TextView txtResumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- VERIFICAÇÃO DE ACEITE ---
        SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
        boolean jaAceitou = pref.getBoolean("aceitou", false);

        if (!jaAceitou) {
            // Se NÃO aceitou, vai para a tela de LGPD
            startActivity(new Intent(this, LgpdActivity.class));
            finish(); // Fecha a MainActivity para não ficar rodando em segundo plano
            return;   // Para a execução do código aqui
        }

        // --- CARREGAMENTO NORMAL DO APP ---
        // Só chega aqui se jaAceitou for true
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));

        // (Aqui você mantém o restante do seu código de swipe e atualizarLista)
    }

    // ... restante dos métodos (onResume, atualizarLista, configurarSwipe) ...
}