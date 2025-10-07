package Supermercado.View;

import javax.swing.*;
import javax.swing.text.*;
import Supermercado.Controller.RegistroController;
import java.awt.*;
import java.text.ParseException;

public class RegistroView extends JFrame {
    private JTextField txtNome;
    private JFormattedTextField txtCpf;
    private JCheckBox chkAdmin;
    private JButton btnSalvar, btnVoltar;
    private RegistroController registerController = new RegistroController();
    private LoginView loginView;

    public RegistroView(LoginView loginView) {
        this.loginView = loginView;

        setTitle("Cadastro de Usuário");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        JLabel lblTitle = new JLabel("Cadastro de Usuário");
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

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formPanel.add(lblNome, gbc);

        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNome.setColumns(20);
        ((AbstractDocument) txtNome.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[a-zA-Zá-úÁ-Ú\\s]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[a-zA-Zá-úÁ-Ú\\s]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(txtNome, gbc);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
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
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtCpf, gbc);

        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        adminPanel.setBackground(new Color(245, 245, 245));
        chkAdmin = new JCheckBox("Administrador");
        chkAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chkAdmin.setBackground(new Color(245, 245, 245));
        adminPanel.add(chkAdmin);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(adminPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnSalvar = createButton("Salvar", new Color(102, 178, 255));
        btnVoltar = createButton("Voltar", new Color(150, 150, 150));

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnVoltar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvarUsuario());
        btnVoltar.addActionListener(e -> {
            loginView.setVisible(true);
            dispose();
        });
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void salvarUsuario() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().replaceAll("\\D", "");
        boolean isAdmin = chkAdmin.isSelected();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        boolean sucesso = registerController.register(nome, cpf, isAdmin);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            loginView.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário!");
        }
    }
}
