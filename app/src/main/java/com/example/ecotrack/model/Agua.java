package com.example.ecotrack.model;

/**
 * Subclasse que representa o recurso Água.
 * Aplica Herança ao estender a classe Recurso.
 */
public class Agua extends Recurso {

    // Construtor que repassa os dados para a classe pai (Recurso)
    public Agua(String nome, String data, double valor) {
        super(nome, data, valor);
    }

    @Override
    public String calcularImpacto() {
        // Regra de negócio: Para cada unidade (ex: m3), estima-se 10 litros salvos
        return "Economia de " + (getValor() * 10) + " litros de água.";
    }
}