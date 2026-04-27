package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecotrack.R;

public class LgpdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lgpd);

        Button btnAceitar = findViewById(R.id.btnAceitar);

        btnAceitar.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            preferences.edit().putBoolean("aceitou_termos", true).apply();
            // Salva a preferência no "disco" do celular
            SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("aceitou", true);
            editor.apply(); // Grava a mudança imediatamente

            // Vai para a tela principal
            Intent intent = new Intent(LgpdActivity.this, MainActivity.class);
            startActivity(intent);

            // Fecha a tela de LGPD para ela sair da "pilha" de telas
            finish();
        });
    }
}