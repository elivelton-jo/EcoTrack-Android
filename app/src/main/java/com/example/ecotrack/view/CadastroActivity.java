package com.example.ecotrack.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;
import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editData, editValor;
    private Spinner spinnerTipo;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = new DbHelper(this);
        editNome = findViewById(R.id.editNome);
        editData = findViewById(R.id.editData);
        editValor = findViewById(R.id.editValor);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        // 1. CONFIGURAÇÃO DO CALENDÁRIO PROFISSIONAL
        editData.setFocusable(false);
        editData.setOnClickListener(v -> mostrarCalendario());

        // 2. CONFIGURAÇÃO DO SPINNER COM CORES CORRIGIDAS (TEXTO PRETO)
        String[] tipos = {"Energia", "Água"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.BLACK); // Texto preto quando fechado
                ((TextView) v).setTextSize(16);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.BLACK); // Texto preto na lista aberta
                ((TextView) v).setPadding(30, 30, 30, 30); // Mais espaçoso
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // 3. BOTÃO SALVAR
        findViewById(R.id.btnSalvar).setOnClickListener(v -> salvar());
    }

    private void mostrarCalendario() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dataFormatada = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
            editData.setText(dataFormatada);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void salvar() {
        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String valorStr = editValor.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        // 4. MENSAGEM PERSONALIZADA (Erro corrigido)
        if (nome.isEmpty() || data.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos, por favor!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            Recurso novoRecurso;

            if (tipo.equals("Energia")) {
                novoRecurso = new Energia(nome, data, valor);
            } else {
                novoRecurso = new Agua(nome, data, valor);
            }

            if (db.inserir(novoRecurso) != -1) {
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar no banco!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Insira um valor numérico válido!", Toast.LENGTH_SHORT).show();
        }
    }
}