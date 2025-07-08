package edulivre.permanencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edulivre.negocios.Curso;

public class CursoDAO {

    public boolean adicionar(Curso curso) throws SQLException {
        String sql = "INSERT INTO Curso (titulo, descricao, avaliacao) VALUES (?, ?, ?::jsonb);";
        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setString(1, curso.getTitulo());
        preparedStatement.setString(2, curso.getDescricao());
        preparedStatement.setString(3, curso.getAvaliacao().toString());
        int linhas = preparedStatement.executeUpdate();
        preparedStatement.close();
        conexao.close();
        return linhas == 1;
    }

    public List<Curso> listarCursosComMediaAvaliacao() throws SQLException {
        String sql = "SELECT c.id, c.titulo, c.descricao, c.data_criacao, " +
                "COALESCE((c.avaliacao->>'media')::numeric, 0) as media_avaliacao, " +
                "COUNT(m.id) as numero_alunos " +
                "FROM Curso c " +
                "LEFT JOIN Matricula m ON m.curso_id = c.id " +
                "GROUP BY c.id";

        List<Curso> cursos = new ArrayList<>();
        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Curso curso = new Curso();
            curso.setId(resultSet.getInt("id"));
            curso.setTitulo(resultSet.getString("titulo"));
            curso.setDescricao(resultSet.getString("descricao"));
            curso.setData_criacao(resultSet.getString("data_criacao"));
            curso.setMediaAvaliacao(resultSet.getDouble("media_avaliacao"));
            curso.setNumeroAlunos(resultSet.getInt("numero_alunos"));

            cursos.add(curso);
        }

        resultSet.close();
        preparedStatement.close();
        conexao.close();

        return cursos;
    }
}