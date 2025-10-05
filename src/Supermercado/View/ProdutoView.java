package Supermercado.View;

import Supermercado.Controller.ProdutoController;
import Supermercado.Model.ProdutoModel;
import Supermercado.Model.UsuarioModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 640, 200);
        add(scroll);

        btnAdd = new JButton("Adicionar");
        btnAdd.setBounds(20, 240, 120, 30);
        add(btnAdd);

        btnEdit = new JButton("Editar");
        btnEdit.setBounds(160, 240, 120, 30);
        add(btnEdit);

        btnRemove = new JButton("Remover");
        btnRemove.setBounds(300, 240, 120, 30);
        add(btnRemove);

        btnLogout = new JButton("Deslogar");
        btnLogout.setBounds(500, 240, 120, 30);
        add(btnLogout);

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnRemove.addActionListener(e -> removeSelected());
        btnLogout.addActionListener(e -> logout());

        refreshTable();
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
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso.");
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao adicionar produto.");
                }
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
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
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Produto atualizado.");
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao atualizar.");
                }
                refreshTable();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void removeSelected() {
        int id = getSelectedProductId();
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }

        Object[] opcoes = {"Sim", "Não"};

        int opt = JOptionPane.showOptionDialog(
                this,
                "Confirma remoção?",
                "Remover",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[1]
        );

        if (opt == 0) {
            boolean ok = controller.deleteProduct(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Produto removido.");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao remover.");
            }
            refreshTable();
        }
    }

    private void logout() {
        int opt = JOptionPane.showConfirmDialog(this, "Deseja deslogar?", "Sair", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }
}
