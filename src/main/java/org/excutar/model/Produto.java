package org.excutar.model;

import java.math.BigDecimal;

public class Produto {
    private int idProduto;
    private int idCategoria;
    private String nome;
    private String descricao;
    private BigDecimal pesoKg;
    private String nomeCategoria;


    // Construtor completo
    public Produto(int idProduto, int idCategoria, String nome, String descricao, BigDecimal pesoKg) {
        this.idProduto = idProduto;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descricao = descricao;
        this.pesoKg = pesoKg;
    }

    // Construtor vazio (essencial para o JavaFX e DAOs)
    public Produto() {}

    // Getters e Setters
    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public String getNomeCategoria() {
        return nomeCategoria; }

    public void setNomeCategoria(String nomeCategoria) {

        this.nomeCategoria = nomeCategoria; }
}