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

    public List<Venda> listar() throws Exception {
        String sql = "SELECT * FROM VENDA";
        List<Venda> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Venda(
                        rs.getInt("ID_Venda"),
                        rs.getInt("ID_Cliente"),
                        rs.getInt("ID_Produto"),
                        rs.getInt("Quantidade_Vendida"),
                        rs.getBigDecimal("Preco_Unidade"),
                        rs.getTimestamp("Data_Hora")
                ));
            }
        }
        return lista;
    }
}
