package Supermercado.Controller;

import Supermercado.Dao.UsuarioDao;
import Supermercado.Model.UsuarioModel;

public class RegistroController {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    public boolean register(String name, String cpf, boolean isAdmin) {
        try {
            if (name == null || name.trim().isEmpty()) {
                System.out.println("O nome não pode estar vazio!");
                return false;
            }
            if (cpf == null || cpf.trim().isEmpty()) {
                System.out.println("O CPF não pode estar vazio!");
                return false;
            }

            UsuarioModel existing = usuarioDao.findByCpf(cpf);
            if (existing != null) {
                System.out.println("CPF já cadastrado!");
                return false;
            }

            UsuarioModel usuarioModel = new UsuarioModel();
            usuarioModel.setNome(name.trim());
            usuarioModel.setCpf(cpf.trim());
            usuarioModel.setAdmin(isAdmin);

            usuarioDao.save(usuarioModel);
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário:");
            e.printStackTrace();
            return false;
        }
    }
}
