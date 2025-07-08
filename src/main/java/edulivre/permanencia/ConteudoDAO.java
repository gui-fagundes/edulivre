package edulivre.permanencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edulivre.negocios.Conteudo;

public class ConteudoDAO {
    
    public List<Conteudo> buscarConteudosPorCurso(int cursoId) throws SQLException {
        String sql = "SELECT id, titulo, descricao, tipo, OCTET_LENGTH(arquivo) as tamanho " +
                     "FROM Conteudo WHERE curso_id = ?";
        
        List<Conteudo> conteudos = new ArrayList<>();
        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, cursoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Conteudo conteudo = new Conteudo();
            conteudo.setId(resultSet.getInt("id"));
            conteudo.setTitulo(resultSet.getString("titulo"));
            conteudo.setDescricao(resultSet.getString("descricao"));
            conteudo.setTipo(resultSet.getString("tipo"));
            conteudos.add(conteudo);
        }
        
        resultSet.close();
        preparedStatement.close();
        conexao.close();
        
        return conteudos;
    }
    
    public boolean adicionar(Conteudo conteudo) throws SQLException {
        String sql = "INSERT INTO Conteudo (curso_id, titulo, descricao, tipo, arquivo) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, conteudo.getCurso().getId());
        preparedStatement.setString(2, conteudo.getTitulo());
        preparedStatement.setString(3, conteudo.getDescricao());
        preparedStatement.setString(4, conteudo.getTipo());
        preparedStatement.setBytes(5, conteudo.getArquivo());
        int linhas = preparedStatement.executeUpdate();
        preparedStatement.close();
        conexao.close();
        return linhas == 1;
    }
}