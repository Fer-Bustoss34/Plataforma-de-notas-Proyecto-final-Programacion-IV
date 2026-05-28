package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    private static final String URL     = "jdbc:postgresql://aws-1-us-west-2.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USUARIO = "postgres.icoymhlszxkfaykdoege";
    private static final String CLAVE   = "proyecto555";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión exitosa a Supabase");
            return conn;
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
