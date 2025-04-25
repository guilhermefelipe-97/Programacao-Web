package com.meuecommerce.dao;

import com.meuecommerce.model.Produto;
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
public class ProdutoDAO {

    @Autowired
    private DatabaseConnection dbConnection;

    // Salvar um produto
    public void save(Produto produto) throws SQLException {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty() ||
                produto.getDescricao() == null || produto.getDescricao().trim().isEmpty() ||
                produto.getPreco() <= 0 || produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Nome, descrição, preço positivo e estoque não-negativo são obrigatórios");
        }
        String sql = "INSERT INTO produto (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.executeUpdate();
        }
    }

    // Buscar produto por ID
    public Produto findById(Long id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setEstoque(rs.getInt("estoque"));
                    return produto;
                }
                return null;
            }
        }
    }

    // Verificar se produto existe por ID
    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // Listar todos os produtos
    public List<Produto> findAll() throws SQLException {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produtos.add(produto);
            }
            return produtos;
        }
    }

    // Excluir produto por ID
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Atualizar estoque de um produto
    public void updateEstoque(Long id, int novoEstoque) throws SQLException {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Produto com ID " + id + " não existe");
        }
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo");
        }
        String sql = "UPDATE produto SET estoque = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }
}