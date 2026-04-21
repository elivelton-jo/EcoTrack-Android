package com.example.ecotrack.view;

import android.app.DatePickerDialog;
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

        // CONFIGURAÇÃO DO CALENDÁRIO
        editData.setFocusable(false); // Impede abrir o teclado
        editData.setOnClickListener(v -> mostrarCalendario());

        String[] tipos = {"Energia", "Água"};
        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos));

        findViewById(R.id.btnSalvar).setOnClickListener(v -> salvar());
    }

    private void mostrarCalendario() {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dataFormatada = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
            editData.setText(dataFormatada);
        }, ano, mes, dia);
        dpd.show();
    }

    private void salvar() {
        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String valorStr = editValor.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (nome.isEmpty() || data.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Preencha tudo!", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor = Double.parseDouble(valorStr);
        Recurso novo = tipo.equals("Energia") ? new Energia(nome, data, valor) : new Agua(nome, data, valor);

        if (db.inserir(novo) != -1) {
            Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}