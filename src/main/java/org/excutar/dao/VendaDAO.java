package org.excutar.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import org.excutar.model.Venda;
import org.excutar.util.Conexao;


public class VendaDAO {
    public void salvar(Venda venda) throws Exception {
        // O PostgreSQL usará o SERIAL para gerar o ID automaticamente
        String sql = "INSERT INTO VENDA (ID_Cliente, ID_Produto, Quantidade_Vendida, Preco_Unidade, Data_Hora) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venda.getIdCliente());
            ps.setInt(2, venda.getIdProduto());
            ps.setInt(3, venda.getQuantidadeVendida());
            ps.setBigDecimal(4, venda.getPrecoUnidade());
            ps.setTimestamp(5, venda.getDataHora());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setIdVenda(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Venda venda) throws Exception {
        String sql = "UPDATE VENDA SET ID_Cliente = ?, ID_Produto = ?, Quantidade_Vendida = ?, Preco_Unidade = ?, Data_Hora = ? WHERE ID_Venda = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, venda.getIdCliente());
            ps.setInt(2, venda.getIdProduto());
            ps.setInt(3, venda.getQuantidadeVendida());
            ps.setBigDecimal(4, venda.getPrecoUnidade());
            ps.setTimestamp(5, venda.getDataHora());
            ps.setInt(6, venda.getIdVenda());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM VENDA WHERE ID_Venda = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Venda buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM VENDA WHERE ID_Venda = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Venda(
                            rs.getInt("ID_Venda"),
                            rs.getInt("ID_Cliente"),
                            rs.getInt("ID_Produto"),
                            rs.getInt("Quantidade_Vendida"),
                            rs.getBigDecimal("Preco_Unidade"),
                            rs.getTimestamp("Data_Hora")
                    );
                }
            }
        }
        return null;
    }

    public List<Venda> listar() {
        List<Venda> vendas = new ArrayList<>();
        
        // SQL corrigido usando os sublinhados do PostgreSQL (id_cliente e id_produto)
        String sql = "SELECT v.*, c.nome AS nome_cliente, p.nome AS nome_produto " +
                    "FROM Venda v " +
                    "LEFT JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                    "LEFT JOIN Produto p ON v.id_produto = p.id_produto";

        try (Connection conn = Conexao.getConnection(); // Confirme se o nome do seu método de conexão é esse mesmo
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda v = new Venda();
                
                // Os nomes aqui dentro das aspas TÊM que ser idênticos às colunas lá no pgAdmin
                v.setIdVenda(rs.getInt("id_venda")); 
                v.setIdCliente(rs.getInt("id_cliente"));
                v.setIdProduto(rs.getInt("id_produto"));
                v.setQuantidadeVendida(rs.getInt("quantidade_vendida")); 
                v.setPrecoUnidade(rs.getBigDecimal("preco_unidade")); 
                v.setDataHora(rs.getTimestamp("data_hora")); 
                
                // Pegando os nomes que o JOIN trouxe
                v.setNomeCliente(rs.getString("nome_cliente"));
                v.setNomeProduto(rs.getString("nome_produto"));

                vendas.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }
        return vendas;
    }
}
