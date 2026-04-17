package com.example.ecotrack.model;

import java.util.Locale;

public class Agua extends Recurso {

    public Agua(String nome, String data, double valor) {
        super(nome, data, valor);
    }

    @Override
    public String calcularImpacto() {
        // Considerando que o valor digitado seja em m³ (metros cúbicos)
        double litros = getValor() * 1000;

        return String.format(Locale.getDefault(),
                "Consumo: %.1f m³ | Economia: %.0f L", getValor(), litros);
    }
}