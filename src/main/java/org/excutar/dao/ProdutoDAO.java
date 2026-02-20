package org.excutar.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.excutar.model.Produto;
import org.excutar.util.Conexao;

public class ProdutoDAO {

    public void salvar(Produto produto) throws Exception {
        // O PostgreSQL usará o SERIAL para gerar o ID automaticamente
        String sql = "INSERT INTO PRODUTO (ID_Categoria, Nome, Descricao, Peso_KG) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, produto.getIdCategoria());
            ps.setString(2, produto.getNome());
            ps.setString(3, produto.getDescricao());
            ps.setBigDecimal(4, produto.getPesoKg());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setIdProduto(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Produto produto) throws Exception {
        String sql = "UPDATE PRODUTO SET ID_Categoria = ?, Nome = ?, Descricao = ?, Peso_KG = ? WHERE ID_Produto = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, produto.getIdCategoria());
            ps.setString(2, produto.getNome());
            ps.setString(3, produto.getDescricao());
            ps.setBigDecimal(4, produto.getPesoKg());
            ps.setInt(5, produto.getIdProduto());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM PRODUTO WHERE ID_Produto = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Produto buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM PRODUTO WHERE ID_Produto = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                            rs.getInt("ID_Produto"),
                            rs.getInt("ID_Categoria"),
                            rs.getString("Nome"),
                            rs.getString("Descricao"),
                            rs.getBigDecimal("Peso_KG")
                    );
                }
            }
        }
        return null;
    }

    public List<Produto> listar() throws Exception {
        String sql = "SELECT ID_Produto, ID_Categoria, Nome, Descricao, Peso_KG FROM PRODUTO ORDER BY Nome";
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("ID_Produto"),
                        rs.getInt("ID_Categoria"),
                        rs.getString("Nome"),
                        rs.getString("Descricao"),
                        rs.getBigDecimal("Peso_KG")
                ));
            }
        }
        return lista;
    }
}