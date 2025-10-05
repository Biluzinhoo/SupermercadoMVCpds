package Supermercado.View;

import javax.swing.*;
import Supermercado.Controller.LoginController;
import Supermercado.Model.UsuarioModel;

public class LoginView extends JFrame {
    private JTextField txtCpf;
    private JButton btnLogin, btnCadastrar;
    private LoginController loginController = new LoginController();

    public LoginView() {
        setTitle("Login - Supermercado");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 30, 100, 25);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(130, 30, 200, 25);
        add(txtCpf);

        btnLogin = new JButton("Entrar");
        btnLogin.setBounds(50, 100, 120, 30);
        add(btnLogin);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(200, 100, 120, 30);
        add(btnCadastrar);

        btnLogin.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CPF!");
                return;
            }

            UsuarioModel usuario = loginController.login(cpf);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bem-vindo " + usuario.getNome());
                if (usuario.isAdmin()) {
                    new ProdutoView(usuario).setVisible(true);
                } else {
                    new CompraView(usuario, this).setVisible(true);
                }
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
            }
        });

        btnCadastrar.addActionListener(e -> {
            new RegistroView(this).setVisible(true);
            setVisible(false);
        });
    }
}
