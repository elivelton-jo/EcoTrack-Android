package com.example.ecotrack.model;

import java.util.Locale;

/**
 * Subclasse Energia.
 * Aqui mostramos o consumo em kWh e o impacto em CO2.
 */
public class Energia extends Recurso {

    public Energia(String nome, String data, double valor) {
        super(nome, data, valor);
    }

    @Override
    public String calcularImpacto() {
        // Cálculo: kWh multiplicado pelo fator de emissão (ex: 0.5)
        double co2 = getValor() * 0.5;

        // Retorna uma String formatada com as duas informações
        return String.format(Locale.getDefault(),
                "Consumo: %.1f kWh | Impacto: %.1f kg CO2", getValor(), co2);
    }
}