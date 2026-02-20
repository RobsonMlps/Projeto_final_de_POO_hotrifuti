package org.excutar.model;

public class Cliente {

    private int idCliente;
    private String cpf, nome;

    public Cliente(int idCliente, String cpf, String nome){
        this.idCliente = idCliente;
        this.cpf = cpf;
        this.nome = nome;
    }

    public Cliente(){}

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    
}