package edulivre.permanencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edulivre.negocios.Usuario;

public class UsuarioDAO {


        public boolean adicionar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (nome, email, senha, perfil) VALUES (?, ?, ?, ?);";
        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setString(1, usuario.getNome());
        preparedStatement.setString(2, usuario.getEmail());
        preparedStatement.setString(3, usuario.getSenha());
        preparedStatement.setString(4, usuario.getPerfil());
        int linhas = preparedStatement.executeUpdate();
        preparedStatement.close();
        conexao.close();
        return linhas == 1;
    }
    
}
