package Supermercado.View;

import javax.swing.*;
import Supermercado.Controller.CompraController;
import Supermercado.Model.UsuarioModel;
import Supermercado.Model.ProdutoModel;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CompraView extends JFrame {
    private UsuarioModel currentUser;
    private LoginView loginView;
    private CompraController controller;

    private JTable tableProducts, tableCart;
    private DefaultTableModel productsModel, cartModel;
    private JLabel lblTotal;
    private JButton btnAddToCart, btnRemoveFromCart, btnCheckout, btnLogout;

    public CompraView(UsuarioModel usuario, LoginView loginRef) {
        this.currentUser = usuario;
        this.loginView = loginRef;
        this.controller = new CompraController();

        setTitle("Compra de Produtos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        productsModel = new DefaultTableModel(new String[]{"ID","Nome","Preço","Estoque"},0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        tableProducts = new JTable(productsModel);
        JScrollPane scrollProds = new JScrollPane(tableProducts);
        scrollProds.setBounds(20,20,400,300);
        add(scrollProds);

        cartModel = new DefaultTableModel(new String[]{"ID","Nome","Preço"},0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        tableCart = new JTable(cartModel);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.setBounds(450,20,400,300);
        add(scrollCart);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setBounds(450,340,200,25);
        add(lblTotal);

        btnAddToCart = new JButton("Adicionar");
        btnAddToCart.setBounds(20,340,200,30);
        add(btnAddToCart);

        btnRemoveFromCart = new JButton("Remover");
        btnRemoveFromCart.setBounds(240,340,200,30);
        add(btnRemoveFromCart);

        btnCheckout = new JButton("Pagar");
        btnCheckout.setBounds(450,380,200,30);
        add(btnCheckout);

        btnLogout = new JButton("Deslogar");
        btnLogout.setBounds(700,380,150,30);
        add(btnLogout);

        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveFromCart.addActionListener(e -> removeFromCart());
        btnCheckout.addActionListener(e -> checkout());
        btnLogout.addActionListener(e -> logout());

        refreshProducts();
        refreshCart();
    }

    private void refreshProducts() {
        productsModel.setRowCount(0);
        List<ProdutoModel> produtos = controller.listAllProducts();
        for (ProdutoModel p : produtos) {
            productsModel.addRow(new Object[]{p.getId(),p.getNome(),p.getPreco(),p.getEstoque()});
        }
    }

    private void refreshCart() {
        cartModel.setRowCount(0);
        for (ProdutoModel p : controller.getCartItems()) {
            cartModel.addRow(new Object[]{p.getId(),p.getNome(),p.getPreco()});
        }
        lblTotal.setText(String.format("Total: R$ %.2f", controller.getTotal()));
    }

    private int getSelectedProductId() {
        int row = tableProducts.getSelectedRow();
        return row < 0 ? -1 : (int) productsModel.getValueAt(row,0);
    }

    private int getSelectedCartId() {
        int row = tableCart.getSelectedRow();
        return row < 0 ? -1 : (int) cartModel.getValueAt(row,0);
    }

    private void addToCart() {
        int id = getSelectedProductId();
        if(id<0){JOptionPane.showMessageDialog(this,"Selecione um produto"); return;}
        if(!controller.addToCart(id)){JOptionPane.showMessageDialog(this,"Falha ao adicionar");}
        refreshCart();
    }

    private void removeFromCart() {
        int id = getSelectedCartId();
        if(id<0){JOptionPane.showMessageDialog(this,"Selecione um produto"); return;}
        if(!controller.removeFromCart(id)){JOptionPane.showMessageDialog(this,"Falha ao remover");}
        refreshCart();
    }

    private void checkout() {
        if(controller.checkout(currentUser.getId())){
            JOptionPane.showMessageDialog(this,"Compra realizada!");
        }else{
            JOptionPane.showMessageDialog(this,"Falha na compra");
        }
        refreshProducts();
        refreshCart();
    }

    private void logout() {
        Object[] options = {"Sim","Não"};
        int opt = JOptionPane.showOptionDialog(
                this,
                "Deseja deslogar?",
                "Sair",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if(opt == 0){
            this.dispose();
            loginView.setVisible(true);
        }
    }
}
