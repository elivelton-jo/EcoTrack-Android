package com.example.ecotrack.model;

/**
 * Classe Sênior: Atua como a base para o Polimorfismo.
 * O atributo 'valor' fica aqui para que todas as subclasses o compartilhem.
 */
public abstract class Recurso {
    private int id;
    private String nome;
    private String data;
    protected double valor; // Atributo protegido para as filhas acessarem se necessário

    // Construtor padrão
    public Recurso(String nome, String data, double valor) {
        this.nome = nome;
        this.data = data;
        this.valor = valor;
    }

    // --- GETTERS E SETTERS ---
    // Essencial para o DbHelper e MainActivity não darem erro
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    /**
     * Método abstrato: Cada filho (Agua/Energia)
     * implementará sua própria lógica de cálculo.
     */
    public abstract String calcularImpacto();
}