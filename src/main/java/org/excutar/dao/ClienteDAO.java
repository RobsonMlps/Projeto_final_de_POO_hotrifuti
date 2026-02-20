package org.excutar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.excutar.model.Cliente;
import org.excutar.util.Conexao;

public class ClienteDAO {
    public List<Cliente> listar() throws Exception {
        String sql = "SELECT * FROM CLIENTE";
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("ID_Cliente"),
                        rs.getString("CPF"),
                        rs.getString("Nome")
                ));
            }
        }
        return lista;
    }
}
