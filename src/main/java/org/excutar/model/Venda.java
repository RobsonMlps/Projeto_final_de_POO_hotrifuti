package org.excutar.model;
import java.math.BigDecimal;
import java.sql.*;

public class Venda {
    private int idVenda;
    private int idCliente;
    private int idProduto;
    private int quantidadeVendida;
    private BigDecimal precoUnidade;
    private Timestamp dataHora;
    private String nomeCliente;
    private String nomeProduto;
    

    public Venda (){}

    public Venda(int idVenda, int idCliente, int idProduto, int quantidadeVendida, BigDecimal precoUnidade,
            Timestamp dataHora) {
        this.idVenda = idVenda;
        this.idCliente = idCliente;
        this.idProduto = idProduto;
        this.quantidadeVendida = quantidadeVendida;
        this.precoUnidade = precoUnidade;
        this.dataHora = dataHora;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public String getNomeCliente() {
        return nomeCliente; 
    }

    public void setNomeCliente(String nomeCliente) { 
        this.nomeCliente = nomeCliente; 
    }

    public String getNomeProduto() { 
        return nomeProduto; 
    }

    public void setNomeProduto(String nomeProduto) { 
        this.nomeProduto = nomeProduto; 
    }
}
