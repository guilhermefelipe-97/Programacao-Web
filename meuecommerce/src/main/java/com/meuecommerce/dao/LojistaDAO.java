package com.meuecommerce.dao;

import com.meuecommerce.model.Lojista;
import com.meuecommerce.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LojistaDAO {
    private final DatabaseConnection dbConnection;

    public LojistaDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public void save(Lojista lojista) throws SQLException {
        String sql = "INSERT INTO lojista (email, nome, senha) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lojista.getEmail());
            stmt.setString(2, lojista.getNome());
            stmt.setString(3, lojista.getSenha());
            stmt.executeUpdate();
        }
    }

    public Lojista findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM lojista WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Lojista lojista = new Lojista();
                lojista.setEmail(rs.getString("email"));
                lojista.setNome(rs.getString("nome"));
                lojista.setSenha(rs.getString("senha"));
                return lojista;
            }
            return null;
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lojista WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}