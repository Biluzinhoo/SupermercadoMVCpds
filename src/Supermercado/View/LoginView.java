package Supermercado.View;

import javax.swing.*;
import javax.swing.text.*;
import Supermercado.Controller.LoginController;
import Supermercado.Model.UsuarioModel;
import java.awt.*;
import java.text.ParseException;

public class LoginView extends JFrame {
    private JFormattedTextField txtCpf;
    private JButton btnLogin, btnCadastrar;
    private LoginController loginController = new LoginController();

    public LoginView() {
        setTitle("Login - Supermercado");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JLabel lblTitle = new JLabel("Supermercado - Login");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblCpf, gbc);

        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(cpfMask);
            txtCpf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        } catch (ParseException e) {
            e.printStackTrace();
            txtCpf = new JFormattedTextField();
        }

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(txtCpf, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(102, 178, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(102, 204, 102));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setFocusPainted(false);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCadastrar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String cpf = txtCpf.getText().replaceAll("\\D", "");

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
