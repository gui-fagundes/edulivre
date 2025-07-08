package edulivre.apresentacao;

import java.sql.SQLException;

import edulivre.negocios.ComentarioAvaliacao;
import edulivre.negocios.Curso;
import edulivre.negocios.Matricula;
import edulivre.negocios.Usuario;
import edulivre.permanencia.ConteudoDAO;
import edulivre.permanencia.CursoDAO;
import edulivre.permanencia.MatriculaDAO;
import edulivre.permanencia.UsuarioDAO;

public class Main {
    public static void main(String[] args) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            CursoDAO cursoDAO = new CursoDAO();
            MatriculaDAO matriculaDAO = new MatriculaDAO();
            ConteudoDAO conteudoDAO = new ConteudoDAO();

            // Criar usuário
            Usuario usuario = new Usuario();
            usuario.setNome("João Silva");
            usuario.setEmail("joao@example.com");
            usuario.setSenha("senha123");
            usuario.setPerfil("aluno");
            usuarioDAO.adicionar(usuario);

            // Criar curso
            Curso curso = new Curso();
            curso.setTitulo("Introdução ao Java");
            curso.setDescricao("Curso básico de programação Java");
            cursoDAO.adicionar(curso);

            // Matricular usuário
            Matricula matricula = new Matricula();
            matricula.setUsuario(usuario);
            matricula.setCurso(curso);
            matriculaDAO.adicionar(matricula);

            // Criar um novo comentário
            ComentarioAvaliacao novoComentario = new ComentarioAvaliacao(
                    1,
                    5,
                    "Excelente curso, aprendi muito!"
            );
            int cursoId = 1;
            boolean sucesso = matriculaDAO.adicionarComentarioAvaliacao(cursoId, novoComentario);

            if (sucesso) {
                matriculaDAO.atualizarMediaAvaliacao(cursoId);
                System.out.println("Comentário adicionado com sucesso!");
            } else {
                System.out.println("Falha ao adicionar comentário.");
            }

            // Listar cursos com média
            System.out.println("Cursos com média de avaliação:");
            cursoDAO.listarCursosComMediaAvaliacao().forEach(c -> {
                System.out.println(c.getTitulo());
            });

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}