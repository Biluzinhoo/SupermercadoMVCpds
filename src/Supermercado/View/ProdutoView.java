package Supermercado.View;

import Supermercado.Controller.ProdutoController;
import Supermercado.Model.ProdutoModel;
import Supermercado.Model.UsuarioModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnRemove, btnLogout;
    private ProdutoController controller;
    private UsuarioModel currentUser;

    public ProdutoView(UsuarioModel usuarioModel) {
        this.currentUser = usuarioModel;
        controller = new ProdutoController();

        setTitle("Gerenciamento de Produtos (Admin)");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        JLabel lblTitle = new JLabel("Gerenciamento de Produtos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnAdd = createButton("Adicionar", new Color(102, 178, 255));
        btnEdit = createButton("Editar", new Color(255, 178, 102));
        btnRemove = createButton("Remover", new Color(255, 102, 102));
        btnLogout = createButton("Deslogar", new Color(150, 150, 150));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnLogout);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnRemove.addActionListener(e -> removeSelected());
        btnLogout.addActionListener(e -> logout());

        refreshTable();
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshTable() {
        try {
            List<ProdutoModel> produtos = controller.listProducts();
            tableModel.setRowCount(0);
            for (ProdutoModel p : produtos) {
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getEstoque()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private int getSelectedProductId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    private void openAddDialog() {
        JTextField tfName = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfStock = new JTextField();
        Object[] fields = {
                "Nome:", tfName,
                "Preço:", tfPrice,
                "Estoque:", tfStock
        };
        int opt = JOptionPane.showConfirmDialog(this, fields, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                String nome = tfName.getText().trim();
                double preco = Double.parseDouble(tfPrice.getText().trim());
                int estoque = Integer.parseInt(tfStock.getText().trim());
                boolean ok = controller.addProduct(nome, preco, estoque);
                JOptionPane.showMessageDialog(this, ok ? "Produto adicionado com sucesso." : "Falha ao adicionar produto.");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos: " + ex.getMessage());
            }
        }
    }

    private void openEditDialog() {
        int id = getSelectedProductId();
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
            return;
        }
        try {
            ProdutoModel p = controller.findById(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
                return;
            }
            JTextField tfName = new JTextField(p.getNome());
            JTextField tfPrice = new JTextField(String.valueOf(p.getPreco()));
            JTextField tfStock = new JTextField(String.valueOf(p.getEstoque()));
            Object[] fields = {
                    "Nome:", tfName,
                    "Preço:", tfPrice,
                    "Estoque:", tfStock
            };
            int opt = JOptionPane.showConfirmDialog(this, fields, "Editar Produto", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                p.setNome(tfName.getText().trim());
                p.setPreco(Double.parseDouble(tfPrice.getText().trim()));
                p.setEstoque(Integer.parseInt(tfStock.getText().trim()));
                boolean ok = controller.updateProduct(p);
                JOptionPane.showMessageDialog(this, ok ? "Produto atualizado." : "Falha ao atualizar.");
                refreshTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void removeSelected() {
        int id = getSelectedProductId();
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }

        Object[] options = {"Sim", "Não"};
        int opt = JOptionPane.showOptionDialog(
                this,
                "Confirma remoção?",
                "Remover",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if (opt == JOptionPane.YES_OPTION) {
            boolean ok = controller.deleteProduct(id);
            JOptionPane.showMessageDialog(this, ok ? "Produto removido." : "Falha ao remover.");
            refreshTable();
        }
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

        if (opt == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }
}
