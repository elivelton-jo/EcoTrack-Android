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
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;
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

        // Inicializa o banco de dados
        db = new DbHelper(this);
        calendario = Calendar.getInstance();

        // Vincula os componentes do XML
        editNome = findViewById(R.id.editNome);
        editData = findViewById(R.id.editData);
        editValor = findViewById(R.id.editValor);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Configura o Spinner (Menu de seleção)
        String[] tipos = {"Água", "Energia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        // Configura o seletor de data (DatePicker)
        editData.setOnClickListener(v -> abrirCalendario());

        // Ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void abrirCalendario() {
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Formata a data para o padrão brasileiro DD/MM/AAAA
            String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
            editData.setText(dataFormatada);
        }, ano, mes, dia);

        picker.show();
    }

    private void salvar() {
        // 1. Coleta os dados
        String nome = editNome.getText().toString().trim();
        String data = editData.getText().toString().trim();
        String valorTexto = editValor.getText().toString().trim();

        // 2. Validação: impede que o app quebre por campos vazios
        if (nome.isEmpty() || data.isEmpty() || valorTexto.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto);
            Recurso novoRecurso;

            // 3. Polimorfismo: Decide qual objeto criar baseado no Spinner
            if (spinnerTipo.getSelectedItemPosition() == 0) {
                // Instancia classe Agua
                novoRecurso = new Agua(nome, data, valor);
            } else {
                // Instancia classe Energia
                novoRecurso = new Energia(nome, data, valor);
            }

            // 4. Persistência: Salva no SQLite
            db.inserir(novoRecurso);

            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela e volta para a principal

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite um valor numérico válido!", Toast.LENGTH_SHORT).show();
        }
    }
}