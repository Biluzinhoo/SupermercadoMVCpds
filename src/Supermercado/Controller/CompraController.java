package Supermercado.Controller;

import Supermercado.Model.Dao.CompraDao;
import Supermercado.Model.Dao.ProdutoDao;
import Supermercado.Model.ProdutoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraController {

    private CompraDao compraDao = new CompraDao();
    private ProdutoDao produtoDao = new ProdutoDao();
    private List<ProdutoModel> cart = new ArrayList<>();
    private List<ProdutoModel> ultimaCompra = new ArrayList<>();

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
                ProdutoModel existing = cart.stream()
                        .filter(p -> p.getId() == productId)
                        .findFirst()
                        .orElse(null);

                if (existing != null) {
                    existing.setQuantidadeComprada(existing.getQuantidadeComprada() + 1);
                } else {
                    produto.setQuantidadeComprada(1);
                    cart.add(produto);
                }
                return true;
            }
            return false;
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
                .mapToDouble(p -> p.getPreco() * p.getQuantidadeComprada())
                .sum();
    }

    public boolean checkout(int userId) {
        if (cart.isEmpty()) {
            return false;
        }

        for (ProdutoModel item : cart) {
            try {
                ProdutoModel produtoDB = produtoDao.findById(item.getId());
                if (produtoDB.getEstoque() < item.getQuantidadeComprada()) {
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }
        }

        ultimaCompra = new ArrayList<>(cart);
        boolean success = finalizePurchase(userId, cart);

        if (success) {
            cart.clear();
        }
        return success;
    }

    private boolean finalizePurchase(int userId, List<ProdutoModel> produtos) {
        try {
            if (produtos == null || produtos.isEmpty()) {
                return false;
            }
            compraDao.savePurchase(userId, produtos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProdutoModel> getUltimaCompra() {
        return new ArrayList<>(ultimaCompra);
    }

    public boolean updateCartQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(productId);
        }

        for (ProdutoModel item : cart) {
            if (item.getId() == productId) {
                try {
                    ProdutoModel produtoDB = produtoDao.findById(productId);
                    if (produtoDB.getEstoque() >= quantity) {
                        item.setQuantidadeComprada(quantity);
                        return true;
                    }
                } catch (SQLException e) {
                    return false;
                }
            }
        }
        return false;
    }
}