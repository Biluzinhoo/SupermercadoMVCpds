package Supermercado.Model.Dao;

import Supermercado.Model.ProdutoModel;

import java.sql.*;
import java.util.List;

public class CompraDao {

    public void savePurchase(int userId, List<ProdutoModel> products) throws SQLException {
        String insertPurchase = "INSERT INTO purchases (user_id, total) VALUES (?, ?)";
        String insertItem = "INSERT INTO purchase_items (purchase_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateStock = "UPDATE products SET stock = stock - ? WHERE id=?";

        double total = products.stream().mapToDouble(ProdutoModel::getPreco).sum();

        try (Connection conn = DBManager.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psPurchase = conn.prepareStatement(insertPurchase, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psItem = conn.prepareStatement(insertItem);
                 PreparedStatement psUpdate = conn.prepareStatement(updateStock)) {

                psPurchase.setInt(1, userId);
                psPurchase.setDouble(2, total);
                psPurchase.executeUpdate();

                ResultSet keys = psPurchase.getGeneratedKeys();
                int purchaseId = 0;
                if (keys.next()) {
                    purchaseId = keys.getInt(1);
                }

                for (ProdutoModel p : products) {
                    psItem.setInt(1, purchaseId);
                    psItem.setInt(2, p.getId());
                    psItem.setInt(3, 1); // por enquanto 1 unidade
                    psItem.executeUpdate();

                    psUpdate.setInt(1, 1);
                    psUpdate.setInt(2, p.getId());
                    psUpdate.executeUpdate();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
