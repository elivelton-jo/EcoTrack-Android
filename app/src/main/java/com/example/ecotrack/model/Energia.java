package com.example.ecotrack.model;

/**
 * Subclasse que representa o recurso Energia.
 * Aplica Polimorfismo ao sobrescrever o método calcularImpacto.
 */
public class Energia extends Recurso {

    public Energia(String nome, String data, double valor) {
        super(nome, data, valor);
    }

    @Override
    public String calcularImpacto() {
        // Regra de negócio: Cálculo de emissão de CO2 baseada no consumo
        return "Redução de " + (getValor() * 0.5) + "kg de CO2.";
    }
}