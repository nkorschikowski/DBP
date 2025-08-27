package Example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class example {

    public static void main(String[] args) throws SQLException, IOException {
        // Property file laden
        Properties props = new Properties();
        String filepath = "./Example/example.properties";
        FileInputStream fis = new FileInputStream(filepath);
        props.load(fis);

        String url = props.getProperty("url");
        String name = props.getProperty("name");
        String password = props.getProperty("password");

        // unter Umständen nötig
        // try {
        // Class.forName("org.postgresql.Driver");
        // } catch (ClassNotFoundException e) {
        // e.printStackTrace();
        // }

        Connection con = DriverManager.getConnection(url, name, password);
        if (con != null) {
            System.out.println("Connected");
        } else {
            System.out.println("Not connected!");
        }

        String sql = "INSERT INTO example (id, name, age, city) VALUES ('8', 'Pepe', '14','Bremen');";

        Statement st = con.createStatement();
        st.executeUpdate(sql);

        // Query
        // ResultSet rs = st.executeQuery(sql);
        // rs.next();
        // String result = rs.getString(1);
        // System.out.println(result);

        con.close();
    }
}