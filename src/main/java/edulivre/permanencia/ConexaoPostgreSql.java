package edulivre.permanencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgreSql {
    private String database;
    private String username;
    private String password;
    private String port;
    private String host;

    public ConexaoPostgreSql(){
        this.host = "localhost";
        this.username = "postgres";
        this.password = "postgres";
        this.port = "5432";
        this.database = "teste";
    }

    public Connection getConexao() throws SQLException{
        String url = "jdbc:postgresql://"+this.host+":"+this.port+"/"+this.database;
        return DriverManager.getConnection(url, username, password);
    }

}
