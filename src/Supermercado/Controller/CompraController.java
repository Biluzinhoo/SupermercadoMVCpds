package Supermercado.Controller;

import Supermercado.Dao.CompraDao;
import Supermercado.Dao.ProdutoDao;
import Supermercado.Model.ProdutoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraController {

    private CompraDao compraDao = new CompraDao();
    private ProdutoDao produtoDao = new ProdutoDao();
    private List<ProdutoModel> cart = new ArrayList<>();

    public List<ProdutoModel> listAllProducts() {
        try {
            return produtoDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<ProdutoModel> getCartItems() {
        return new ArrayList<>(cart);
    }

    public boolean addToCart(int productId) {
        try {
            ProdutoModel produto = produtoDao.findById(productId);
            if (produto != null && produto.getEstoque() > 0) {
                cart.add(produto);
                return true;
            } else {
                System.out.println("Produto não encontrado ou sem estoque!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(int productId) {
        return cart.removeIf(p -> p.getId() == productId);
    }

    public double getTotal() {
        return cart.stream()
                .mapToDouble(ProdutoModel::getPreco)
                .sum();
    }

    public boolean checkout(int userId) {
        if (cart.isEmpty()) {
            System.out.println("Carrinho vazio!");
            return false;
        }
        boolean success = finalizePurchase(userId, cart);
        if (success) {
            cart.clear();
        }
        return success;
    }

    public boolean finalizePurchase(int userId, List<ProdutoModel> produtos) {
        try {
            if (produtos == null || produtos.isEmpty()) {
                System.out.println("Carrinho vazio!");
                return false;
            }
            compraDao.savePurchase(userId, produtos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
