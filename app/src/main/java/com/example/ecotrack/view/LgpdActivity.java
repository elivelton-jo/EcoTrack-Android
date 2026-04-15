package com.example.ecotrack.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecotrack.R;

public class LgpdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lgpd);

        Button btnAceitar = findViewById(R.id.btnAceitar);

        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Salva nas preferências que o usuário aceitou (Persistência de Aceite)
                SharedPreferences pref = getSharedPreferences("EcoTrackPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("aceitou_lgpd", true);
                editor.apply();

                // Vai para a Main e fecha esta tela
                startActivity(new Intent(LgpdActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}