package org.excutar.util; // ALTERAÇÃO OBRIGATÓRIA: Mude o pacote para o novo nome

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/HortifrutiPOO";
    private static final String USER = "postgres";
    private static final String PASS = "12345678";

    // Recomendação: Usar apenas SQLException no throws para simplificar o seu DAO
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado!", e);
        }
    }

    /*public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("------------------------------------------");
                System.out.println("✅ CONEXÃO REALIZADA COM SUCESSO!");
                System.out.println("Projeto: Hortifruti");
                System.out.println("Banco: PostgreSQL");
                System.out.println("------------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("❌ FALHA NA CONEXÃO:");
            e.printStackTrace();
        }
    }*/
}