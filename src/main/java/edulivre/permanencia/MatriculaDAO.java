package edulivre.permanencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edulivre.negocios.ComentarioAvaliacao;
import edulivre.negocios.Matricula;

public class MatriculaDAO {

    public boolean adicionar(Matricula matricula) throws SQLException {
        String verificaSql = "SELECT id FROM Matricula WHERE usuario_id = ? AND curso_id = ?";
        Connection conexao = new ConexaoPostgreSql().getConexao();

        PreparedStatement verificaStmt = conexao.prepareStatement(verificaSql);
        verificaStmt.setInt(1, matricula.getUsuario().getId());
        verificaStmt.setInt(2, matricula.getCurso().getId());
        boolean existe = verificaStmt.executeQuery().next();
        verificaStmt.close();

        if (existe) {
            conexao.close();
            return false;
        }

        String sql = "INSERT INTO Matricula (usuario_id, curso_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, matricula.getUsuario().getId());
        preparedStatement.setInt(2, matricula.getCurso().getId());
        int linhas = preparedStatement.executeUpdate();
        preparedStatement.close();
        conexao.close();
        return linhas == 1;
    }

    public boolean adicionarComentarioAvaliacao(int cursoId, ComentarioAvaliacao comentario) throws SQLException {
        String sql = "UPDATE Curso SET avaliacao = " +
                "CASE " +
                "  WHEN avaliacao IS NULL THEN " +
                "    jsonb_build_object(" +
                "      'media', 0, " +
                "      'comentarios', jsonb_build_array(?::jsonb)" +
                "    ) " +
                "  ELSE " +
                "    jsonb_set(" +
                "      avaliacao, " +
                "      '{comentarios}', " +
                "      COALESCE(avaliacao->'comentarios', '[]'::jsonb) || ?::jsonb, " +
                "      true" +
                "    ) " +
                "END " +
                "WHERE id = ?";

        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);

        String comentarioJson = comentario.toJson();
        preparedStatement.setString(1, comentarioJson);
        preparedStatement.setString(2, comentarioJson);
        preparedStatement.setInt(3, cursoId);

        int linhasAfetadas = preparedStatement.executeUpdate();

        preparedStatement.close();
        conexao.close();

        return linhasAfetadas == 1;
    }

    public boolean atualizarMediaAvaliacao(int cursoId) throws SQLException {
        String sql = "UPDATE Curso SET avaliacao = " +
                "jsonb_set(" +
                "  COALESCE(avaliacao, '{\"comentarios\":[]}'::jsonb), " +
                "  '{media}', " +
                "  to_jsonb(COALESCE(" +
                "    (SELECT AVG((comentario->>'nota')::numeric) " +
                "     FROM jsonb_array_elements(avaliacao->'comentarios') comentario), 0" +
                "  )) " +
                ") " +
                "WHERE id = ?";

        Connection conexao = new ConexaoPostgreSql().getConexao();
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, cursoId);

        int linhasAfetadas = preparedStatement.executeUpdate();

        preparedStatement.close();
        conexao.close();

        return linhasAfetadas == 1;
    }
}