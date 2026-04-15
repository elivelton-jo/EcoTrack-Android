package com.example.ecotrack.model;

public class Energia extends Recurso {

    private double kwh;

    public Energia() {
        super();
    }

    // Getter e Setter para o atributo específico
    public double getKwh() {
        return kwh;
    }

    public void setKwh(double kwh) {
        this.kwh = kwh;
    }

    // Polimorfismo: implementando a lógica específica para Energia
    @Override
    public String calcularImpacto() {
        // Exemplo: Cálculo fictício de emissão de CO2 (0.5kg por kWh)
        double co2 = this.kwh * 0.5;
        return "Impacto: Foram gerados " + co2 + "kg de CO2 na atmosfera.";
    }
}