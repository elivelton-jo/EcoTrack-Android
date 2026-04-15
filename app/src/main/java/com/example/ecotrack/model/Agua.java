package com.example.ecotrack.model;

// 'extends' indica que Agua é filha de Recurso (Herança)
public class Agua extends Recurso {

    private double metrosCubicos;

    public Agua() {
        super(); // Chama o construtor da classe pai
    }

    // Getter e Setter para o atributo específico
    public double getMetrosCubicos() {
        return metrosCubicos;
    }

    public void setMetrosCubicos(double metrosCubicos) {
        this.metrosCubicos = metrosCubicos;
    }

    // Polimorfismo: implementando a lógica específica para Água
    @Override
    public String calcularImpacto() {
        // Exemplo: converter m3 para litros para mostrar o impacto
        double litros = this.metrosCubicos * 1000;
        return "Impacto: Foram consumidos " + litros + " litros de água.";
    }
}