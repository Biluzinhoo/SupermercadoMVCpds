package Supermercado.Controller;

import Supermercado.Dao.ProdutoDao;
import Supermercado.Model.ProdutoModel;

import java.util.List;

public class ProdutoController {

    private ProdutoDao produtoDao = new ProdutoDao();

    public boolean addProduct(String nome, double preco, int stock) {
        try {
            ProdutoModel produto = new ProdutoModel();
            produto.setNome(nome);
            produto.setPreco(preco);
            produto.setEstoque(stock);

            produtoDao.save(produto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProdutoModel> listProducts() {
        try {
            List<ProdutoModel> produtos = produtoDao.findAll();
            return produtos;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public ProdutoModel findById(int id) {
        try {
            return produtoDao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateProduct(ProdutoModel produto) {
        try {
            if (produto == null) return false;
            produtoDao.update(produto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        try {
            produtoDao.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}