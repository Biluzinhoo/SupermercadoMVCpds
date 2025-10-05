package Supermercado.View;

import javax.swing.*;
import Supermercado.Controller.RegistroController;

public class RegistroView extends JFrame {
    private JTextField txtNome, txtCpf;
    private JCheckBox chkAdmin;
    private JButton btnSalvar, btnVoltar;
    private RegistroController registerController = new RegistroController();
    private LoginView loginView;

    public RegistroView(LoginView loginView) {
        this.loginView = loginView;

        setTitle("Cadastro de Usuário");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(130, 30, 200, 25);
        add(txtNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 70, 100, 25);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(130, 70, 200, 25);
        add(txtCpf);

        chkAdmin = new JCheckBox("Administrador");
        chkAdmin.setBounds(130, 110, 200, 25);
        add(chkAdmin);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(50, 160, 120, 30);
        add(btnSalvar);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(200, 160, 120, 30);
        add(btnVoltar);

        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().trim();
            boolean isAdmin = chkAdmin.isSelected();

            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                return;
            }

            boolean sucesso = registerController.register(nome, cpf, isAdmin);
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                loginView.setVisible(true); // Volta para o login
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário!");
            }
        });

        btnVoltar.addActionListener(e -> {
            loginView.setVisible(true);
            dispose();
        });
    }
}
