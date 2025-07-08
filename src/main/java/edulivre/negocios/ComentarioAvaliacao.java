package edulivre.negocios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComentarioAvaliacao {
    private int usuarioId;
    private int nota;
    private String comentario;
    private String data;

    public ComentarioAvaliacao(int usuarioId, int nota, String comentario) {
        this.usuarioId = usuarioId;
        this.nota = nota;
        this.comentario = comentario;
        this.data = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public String getData() {
        return data;
    }

    public String toJson() {
        return String.format(
                "{\"usuario_id\": %d, \"nota\": %d, \"comentario\": \"%s\", \"data\": \"%s\"}",
                usuarioId, nota, comentario, data);
    }
}