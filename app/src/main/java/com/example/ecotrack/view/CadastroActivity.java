package com.example.ecotrack.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecotrack.R;
import com.example.ecotrack.database.DbHelper;
import java.util.Calendar;
import java.util.Locale;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editData, editValor;
    private Spinner spinnerTipo;
    private Button btnSalvar;
    private DbHelper db;
    private Calendar calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        db = new DbHelper(this);
        calendario = Calendar.getInstance();

        // Inicializa os componentes
        editNome = findViewById(R.id.editNome);
        editData = findViewById(R.id.editData);
        editValor = findViewById(R.id.editValor);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Configura o Spinner
        String[] tipos = {"Água", "Energia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        // --- CONFIGURAÇÃO DO SELETOR DE DATA (CALENDÁRIO) ---
        editData.setOnClickListener(v -> abrirCalendario());

        // Ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void abrirCalendario() {
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Formata a data para exibir no campo (DD/MM/AAAA)
            String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
            editData.setText(dataFormatada);
        }, ano, mes, dia);

        // Opcional: Impedir que o usuário selecione uma data no futuro
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());

        picker.show();
    }

    private void salvar() {
        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String valorStr = editValor.getText().toString();

        // Validação simples
        if (nome.isEmpty() || data.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            boolean sucesso = db.salvarRecurso(nome, data, tipo, valor);

            if (sucesso) {
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show();
        }
    }
}