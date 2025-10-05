package Supermercado.Controller;

import Supermercado.Dao.UsuarioDao;
import Supermercado.Model.UsuarioModel;

public class LoginController {

    private UsuarioDao usuarioDao = new UsuarioDao();

    public UsuarioModel login(String cpf) {
        try {
            UsuarioModel usuario = usuarioDao.findByCpf(cpf);
            if (usuario != null) {
                return usuario;
            } else {
                System.out.println("Usuário não encontrado!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
