package Supermercado.Model.Dao;

import Supermercado.Model.ProdutoModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    public void save(ProdutoModel produtoModel) throws SQLException {
        String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produtoModel.getNome());
            stmt.setDouble(2, produtoModel.getPreco());
            stmt.setInt(3, produtoModel.getEstoque());
            stmt.executeUpdate();
        }
    }

    public List<ProdutoModel> findAll() throws SQLException {
        List<ProdutoModel> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new ProdutoModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }
        return products;
    }

    public void update(ProdutoModel produtoModel) throws SQLException {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produtoModel.getNome());
            stmt.setDouble(2, produtoModel.getPreco());
            stmt.setInt(3, produtoModel.getEstoque());
            stmt.setInt(4, produtoModel.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM purchase_items WHERE product_id=?";
        String sqlDelete = "DELETE FROM products WHERE id=?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {

            stmtCheck.setInt(1, id);
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Não é possível deletar o produto, existem compras associadas.");
            }

            stmtDelete.setInt(1, id);
            stmtDelete.executeUpdate();
        }
    }

    public ProdutoModel findById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProdutoModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }
        }
        return null;
    }

    public void updateStock(int id, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock=? WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}
