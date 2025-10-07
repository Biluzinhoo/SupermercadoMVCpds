package Supermercado.View;

import javax.swing.*;
import Supermercado.Controller.CompraController;
import Supermercado.Model.UsuarioModel;
import Supermercado.Model.ProdutoModel;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        productsModel = new DefaultTableModel(new String[]{"ID","Nome","Preço","Estoque"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tableProducts = new JTable(productsModel);
        tableProducts.setFillsViewportHeight(true);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollProds = new JScrollPane(tableProducts);
        scrollProds.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));
        scrollProds.setPreferredSize(new Dimension(400,300));

        cartModel = new DefaultTableModel(new String[]{"ID","Nome","Preço"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tableCart = new JTable(cartModel);
        tableCart.setFillsViewportHeight(true);
        tableCart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.setBorder(BorderFactory.createTitledBorder("Carrinho"));
        scrollCart.setPreferredSize(new Dimension(400,300));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(scrollProds);
        centerPanel.add(scrollCart);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(245, 245, 245));
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        btnAddToCart = new JButton("Adicionar");
        btnAddToCart.setBackground(new Color(102, 178, 255));
        btnAddToCart.setForeground(Color.WHITE);

        btnRemoveFromCart = new JButton("Remover");
        btnRemoveFromCart.setBackground(new Color(255, 102, 102));
        btnRemoveFromCart.setForeground(Color.WHITE);

        btnCheckout = new JButton("Pagar");
        btnCheckout.setBackground(new Color(102, 204, 102));
        btnCheckout.setForeground(Color.WHITE);

        btnLogout = new JButton("Deslogar");
        btnLogout.setBackground(new Color(150, 150, 150));
        btnLogout.setForeground(Color.WHITE);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        actionPanel.add(btnAddToCart);
        actionPanel.add(btnRemoveFromCart);
        actionPanel.add(lblTotal);
        actionPanel.add(btnCheckout);
        actionPanel.add(btnLogout);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

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
        int opt = JOptionPane.showConfirmDialog(this,"Deseja deslogar?","Sair",JOptionPane.YES_NO_OPTION);
        if(opt == JOptionPane.YES_OPTION){
            this.dispose();
            loginView.setVisible(true);
        }
    }
}
