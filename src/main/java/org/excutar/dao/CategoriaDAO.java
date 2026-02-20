package org.excutar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.excutar.model.Categoria;
import org.excutar.util.Conexao;

public class CategoriaDAO {
    public List<Categoria> listar() throws Exception {
        String sql = "SELECT * FROM CATEGORIA";
        List<Categoria> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("ID_Categoria"),
                        rs.getString("Nome"),
                        rs.getString("Descricao")
                ));
            }
        }
        return lista;
    }
}
