package com.example.ecotrack.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editData, editValor;
    private Spinner spinnerTipo;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = new DbHelper(this);

        // Inicializa os componentes do XML
        editNome = findViewById(R.id.editNome);
        editData = findViewById(R.id.editData);
        editValor = findViewById(R.id.editValor);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        // Configura as opções do Spinner
        String[] tipos = {"Energia", "Água"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        // Botão Salvar
        findViewById(R.id.btnSalvar).setOnClickListener(v -> salvar());
    }

    private void salvar() {
        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String valorStr = editValor.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        // Validação simples
        if (nome.isEmpty() || data.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor = Double.parseDouble(valorStr);
        Recurso novoRecurso;

        // Verifica o tipo selecionado para criar o objeto correto
        if (tipo.equals("Energia")) {
            novoRecurso = new Energia(nome, data, valor);
        } else {
            novoRecurso = new Agua(nome, data, valor);
        }

        // Tenta salvar no banco de dados
        long id = db.inserir(novoRecurso);

        if (id != -1) {
            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela e volta para a MainActivity
        } else {
            Toast.makeText(this, "Erro ao salvar no banco!", Toast.LENGTH_SHORT).show();
        }
    }
}