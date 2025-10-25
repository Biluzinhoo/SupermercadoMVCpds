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
    private JButton btnAddToCart, btnRemoveFromCart, btnCheckout, btnLogout, btnUpdateQuantity;

    public CompraView(UsuarioModel usuario, LoginView loginRef) {
        this.currentUser = usuario;
        this.loginView = loginRef;
        this.controller = new CompraController();

        initializeUI();
        setupEventListeners();
        refreshProducts();
        refreshCart();
    }

    private void initializeUI() {
        setTitle("Compra de Produtos - " + currentUser.getNome());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        productsModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProducts = createTable(productsModel);
        JScrollPane scrollProds = new JScrollPane(tableProducts);
        scrollProds.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));
        scrollProds.setPreferredSize(new Dimension(500, 300));

        cartModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Qtd", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCart = createTable(cartModel);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.setBorder(BorderFactory.createTitledBorder("Carrinho"));
        scrollCart.setPreferredSize(new Dimension(500, 300));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(scrollProds);
        centerPanel.add(scrollCart);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(new Color(245, 245, 245));

        btnAddToCart = createButton("Adicionar", new Color(102, 178, 255));
        btnRemoveFromCart = createButton("Remover", new Color(255, 102, 102));
        btnUpdateQuantity = createButton("Alterar Qtd", new Color(255, 178, 102));
        btnCheckout = createButton("Finalizar Compra", new Color(102, 204, 102));
        btnLogout = createButton("Deslogar", new Color(150, 150, 150));

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        actionPanel.add(btnAddToCart);
        actionPanel.add(btnRemoveFromCart);
        actionPanel.add(btnUpdateQuantity);
        actionPanel.add(lblTotal);
        actionPanel.add(btnCheckout);
        actionPanel.add(btnLogout);

        return actionPanel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        return button;
    }

    private void setupEventListeners() {
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveFromCart.addActionListener(e -> removeFromCart());
        btnUpdateQuantity.addActionListener(e -> updateQuantity());
        btnCheckout.addActionListener(e -> checkout());
        btnLogout.addActionListener(e -> logout());
    }

    private void refreshProducts() {
        SwingUtilities.invokeLater(() -> {
            productsModel.setRowCount(0);
            List<ProdutoModel> produtos = controller.listAllProducts();
            for (ProdutoModel p : produtos) {
                productsModel.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        String.format("R$ %.2f", p.getPreco()),
                        p.getEstoque()
                });
            }
        });
    }

    private void refreshCart() {
        SwingUtilities.invokeLater(() -> {
            cartModel.setRowCount(0);
            for (ProdutoModel p : controller.getCartItems()) {
                double subtotal = p.getPreco() * p.getQuantidadeComprada();
                cartModel.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        String.format("R$ %.2f", p.getPreco()),
                        p.getQuantidadeComprada(),
                        String.format("R$ %.2f", subtotal)
                });
            }
            lblTotal.setText(String.format("Total: R$ %.2f", controller.getTotal()));
        });
    }

    private int getSelectedProductId() {
        int row = tableProducts.getSelectedRow();
        return row < 0 ? -1 : (int) productsModel.getValueAt(row, 0);
    }

    private int getSelectedCartId() {
        int row = tableCart.getSelectedRow();
        return row < 0 ? -1 : (int) cartModel.getValueAt(row, 0);
    }

    private void addToCart() {
        int id = getSelectedProductId();
        if (id < 0) {
            showMessage("Selecione um produto da lista para adicionar ao carrinho");
            return;
        }

        if (controller.addToCart(id)) {
            refreshCart();
            refreshProducts();
        } else {
            showMessage("Falha ao adicionar produto ao carrinho. Verifique o estoque.");
        }
    }

    private void removeFromCart() {
        int id = getSelectedCartId();
        if (id < 0) {
            showMessage("Selecione um produto do carrinho para remover");
            return;
        }

        if (controller.removeFromCart(id)) {
            refreshCart();
            refreshProducts();
        } else {
            showMessage("Falha ao remover produto do carrinho");
        }
    }

    private void updateQuantity() {
        int id = getSelectedCartId();
        if (id < 0) {
            showMessage("Selecione um produto do carrinho para alterar a quantidade");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Nova quantidade:");
        if (input != null) {
            try {
                int quantity = Integer.parseInt(input);
                if (controller.updateCartQuantity(id, quantity)) {
                    refreshCart();
                    refreshProducts();
                } else {
                    showMessage("Quantidade indisponível em estoque");
                }
            } catch (NumberFormatException e) {
                showMessage("Digite um número válido");
            }
        }
    }

    private void checkout() {
        if (controller.getCartItems().isEmpty()) {
            showMessage("O carrinho está vazio!");
            return;
        }

        Object[] options = {"Sim", "Não"};
        int confirm = JOptionPane.showOptionDialog(
                this,
                "Confirmar finalização da compra?",
                "Finalizar Compra",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (confirm == JOptionPane.YES_OPTION) {
        }


        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.checkout(currentUser.getId())) {
                showMessage("Compra realizada com sucesso!");
                mostrarCupomFiscal();
                refreshProducts();
                refreshCart();
            } else {
                showMessage("Falha na compra! Verifique o estoque dos produtos.");
            }
        }
    }

    private void mostrarCupomFiscal() {
        List<ProdutoModel> itens = controller.getUltimaCompra();
        if (itens.isEmpty()) return;

        StringBuilder cupom = new StringBuilder();
        cupom.append("        SUPERMERCADO JAVA\n");
        cupom.append("      *** CUPOM FISCAL ***\n");
        cupom.append("----------------------------------------\n");
        cupom.append("Cliente: ").append(currentUser.getNome()).append("\n");
        cupom.append("CPF: ").append(formatCPF(currentUser.getCpf())).append("\n");
        cupom.append("Data: ").append(java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("%-15s %4s %8s %10s\n", "Produto", "Qtd", "Preço", "Subtotal"));
        cupom.append("----------------------------------------\n");

        double total = 0.0;
        for (ProdutoModel p : itens) {
            int qtd = p.getQuantidadeComprada();
            double subtotal = p.getPreco() * qtd;
            total += subtotal;
            cupom.append(String.format("%-15s %4d %8.2f %10.2f\n",
                    truncate(p.getNome(), 15), qtd, p.getPreco(), subtotal));
        }

        cupom.append("----------------------------------------\n");
        cupom.append(String.format("TOTAL: R$ %.2f\n", total));
        cupom.append("----------------------------------------\n");
        cupom.append("Obrigado pela preferência!\n");

        JTextArea textArea = new JTextArea(cupom.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scroll, "Cupom Fiscal", JOptionPane.INFORMATION_MESSAGE);
    }

    private String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }

    private String formatCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private void logout() {
        Object[] options = {"Sim", "Não"};
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

        if (opt == 0) {
            this.dispose();
            loginView.setVisible(true);
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}