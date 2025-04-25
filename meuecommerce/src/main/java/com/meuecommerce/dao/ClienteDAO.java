package com.meuecommerce.dao;

import com.meuecommerce.model.Cliente;
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
public class ClienteDAO {

    @Autowired
    private DatabaseConnection dbConnection;

    // Salvar um cliente
    public void save(Cliente cliente) throws SQLException {
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty() ||
                cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
                cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Email, nome e senha são obrigatórios");
        }
        String sql = "INSERT INTO cliente (email, nome, senha) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getEmail());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getSenha());
            stmt.executeUpdate();
        }
    }

    // Buscar cliente por email
    public Cliente findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setEmail(rs.getString("email"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setSenha(rs.getString("senha"));
                    return cliente;
                }
                return null;
            }
        }
    }

    // Verificar se cliente existe por email
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cliente WHERE email = ?";
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

    // Listar todos os clientes
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setEmail(rs.getString("email"));
                cliente.setNome(rs.getString("nome"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
            return clientes;
        }
    }

    // Excluir cliente por email
    public void deleteByEmail(String email) throws SQLException {
        String sql = "DELETE FROM cliente WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        }
    }
}