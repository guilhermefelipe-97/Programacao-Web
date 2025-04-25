package com.meuecommerce.dao;

import com.meuecommerce.model.Lojista;
import com.meuecommerce.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LojistaDAO {

    @Autowired
    private DatabaseConnection dbConnection;

    // Salvar um lojista
    public void save(Lojista lojista) throws SQLException {
        if (lojista.getEmail() == null || lojista.getEmail().trim().isEmpty() ||
                lojista.getNome() == null || lojista.getNome().trim().isEmpty() ||
                lojista.getSenha() == null || lojista.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Email, nome e senha são obrigatórios");
        }
        String sql = "INSERT INTO lojista (email, nome, senha) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lojista.getEmail());
            stmt.setString(2, lojista.getNome());
            stmt.setString(3, lojista.getSenha());
            stmt.executeUpdate();
        }
    }

    // Buscar lojista por email
    public Lojista findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM lojista WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
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
    }

    // Verificar se lojista existe por email
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lojista WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // Listar todos os lojistas
    public List<Lojista> findAll() throws SQLException {
        String sql = "SELECT * FROM lojista";
        List<Lojista> lojistas = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Lojista lojista = new Lojista();
                lojista.setEmail(rs.getString("email"));
                lojista.setNome(rs.getString("nome"));
                lojista.setSenha(rs.getString("senha"));
                lojistas.add(lojista);
            }
            return lojistas;
        }
    }

    // Excluir lojista por email
    public void deleteByEmail(String email) throws SQLException {
        String sql = "DELETE FROM lojista WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        }
    }
}