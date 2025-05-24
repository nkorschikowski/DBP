package Example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class exampleCSV {
    public static void main(String[] args) {
Properties props = new Properties();
        String filepath = "./Example/example.properties";
        try {
            FileInputStream fis = new FileInputStream(filepath);
            props.load(fis);
        } catch (IOException g) {
            System.out.println("Inputsream Excaption:" + g);
        }

        String url = props.getProperty("url");
        String name = props.getProperty("name");
        String password = props.getProperty("password");

        // String filepath = "data\\reviews.csv";
        String sql = "INSERT INTO example (id, name, age, city) VALUES (?, ?, ?, ?)";

        BufferedReader reader = null;
        String line = " ";
        String csvpath="./Example/example.csv";

        try {
            reader = new BufferedReader(new FileReader(csvpath));
            String headerLine = reader.readLine();
            Connection con = DriverManager.getConnection(url, name, password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not connected!");
            }
            PreparedStatement statement = con.prepareStatement(sql);

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                int id = Integer.parseInt(data[0]);
                String names = data[1];
                int age = Integer.parseInt(data[2]);
                String city = data.length == 4 ? data[3] : "";
                statement.setInt(1, id);
                statement.setString(2, names);
                statement.setInt(3, age);
                statement.setString(4, city);
                statement.executeUpdate();
                
                System.out.print(id + "          ");
                
                System.out.println();
            }
        } catch (IOException d) {
            d.printStackTrace();
        }catch (SQLException f) {
            f.printStackTrace();
        }
    }
}
