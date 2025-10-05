package Supermercado.Model;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoModel {
    private List<ProdutoModel> produtos = new ArrayList<>();

    public void addProduct(ProdutoModel produtoModel) {
        produtos.add(produtoModel);
    }

    public void removeProduct(ProdutoModel produtoModel) {
        produtos.remove(produtoModel);
    }

    public double getTotal() {
        return produtos.stream().mapToDouble(ProdutoModel::getPreco).sum();
    }

    public List<ProdutoModel> getProducts() {
        return produtos;
    }

    public void clear() {
        produtos.clear();
    }
}
