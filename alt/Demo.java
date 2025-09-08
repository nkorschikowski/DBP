
// Source code is decompiled from a .class file using FernFlower decompiler.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {
    public Demo() {
    }

    public static void main(String[] var0) throws SQLException {
        String sql = "Select * FROM kunden";
        String url = "jdbc:postgresql://localhost:5432/TestDBP";
        String name = "postgres";
        String password = "postgres";
        Connection con = DriverManager.getConnection(url, name, password);
        System.out.println("Connection successfull");
        Statement st = con.createStatement();
        st.executeQuery(sql);
    }
}