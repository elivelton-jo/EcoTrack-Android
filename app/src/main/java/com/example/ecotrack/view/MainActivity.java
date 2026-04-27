package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater; // Adicionado
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import com.example.ecotrack.model.Recurso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog; // Para a ajuda

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtResumo;
    private RecyclerView recycler;
    private FloatingActionButton btnNovo, btnAjuda;
    private DbHelper db;
    private RecursoAdapter adapter; // AQUI: Estava faltando declarar o adapter!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VERIFICAÇÃO LGPD
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean aceitouTermos = preferences.getBoolean("aceitou_termos", false);

        if (!aceitouTermos) {
            Intent intent = new Intent(this, LgpdActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        txtResumo = findViewById(R.id.txtResumoImpacto);
        recycler = findViewById(R.id.recyclerRecursos);
        btnNovo = findViewById(R.id.btnNovo);
        btnAjuda = findViewById(R.id.btnAjuda);
        db = new DbHelper(this);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Animações
        btnNovo.setScaleX(0f);
        btnNovo.setScaleY(0f);
        btnNovo.animate().scaleX(1f).scaleY(1f).setDuration(500).setStartDelay(200).start();

        btnAjuda.setScaleX(0f);
        btnAjuda.setScaleY(0f);
        btnAjuda.animate().scaleX(1f).scaleY(1f).setDuration(500).setStartDelay(400).start();

        btnNovo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CadastroActivity.class));
        });

        btnAjuda.setOnClickListener(v -> mostrarAjuda());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDados();
    }

    private void carregarDados() {
        List<Recurso> novaLista = db.listarTodos();

        // Atualiza o resumo no topo (kWh | Litros)
        if (db.gerarResumo() != null) {
            txtResumo.setText(db.gerarResumo());
        }

        if (adapter == null) {
            adapter = new RecursoAdapter(novaLista);
            recycler.setAdapter(adapter);
        } else {
            adapter.setLista(novaLista);
            adapter.notifyDataSetChanged();
        }
    }

    private void mostrarAjuda() {
        // Lógica profissional para o card de ajuda subir
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_ajuda, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}