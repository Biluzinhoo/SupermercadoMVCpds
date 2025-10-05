package Supermercado.Dao;

import Supermercado.Model.UsuarioModel;
import java.sql.*;

public class UsuarioDao {

    public void save(UsuarioModel usuario) throws SQLException {
        String sql = "INSERT INTO users (name, cpf, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome() != null ? usuario.getNome() : "");
            stmt.setString(2, usuario.getCpf() != null ? usuario.getCpf() : "");
            stmt.setBoolean(3, usuario.isAdmin());

            stmt.executeUpdate();
        }
    }

    public UsuarioModel findByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM users WHERE cpf = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UsuarioModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cpf"),
                        rs.getBoolean("is_admin")
                );
            }
        }
        return null;
    }
}
