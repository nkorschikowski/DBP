import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;



public class exampleXML {

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

        String sql = "INSERT INTO example (id, name, age, city) VALUES (?, ?, ?, ?)";

        BufferedReader reader;
        String line;
        String xmlpath="./Example/example.xml";

        try {
            reader = new BufferedReader(new FileReader(xmlpath));
            String headerLine = reader.readLine();
            Connection con = DriverManager.getConnection(url, name, password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not connected!");
            }
            reader.close();
            PreparedStatement statement = con.prepareStatement(sql);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xmlreader = factory.createXMLStreamReader(new FileInputStream(xmlpath));

            int id = 0;
            int age = 0;
            name = null;
            String city = null;
            String currentElement = "";

            while (xmlreader.hasNext()) {
                int event = xmlreader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    currentElement = xmlreader.getLocalName();
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    String text = xmlreader.getText().trim();
                    if (!text.isEmpty()) {
                        switch (currentElement) {
                            case "id":
                                id = Integer.parseInt(text);
                                break;
                            case "name":
                                name = text;
                                break;
                            case "age":
                                age = Integer.parseInt(text);
                                break;
                            case "city":
                                city = text;
                                break;
                        }
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if (xmlreader.getLocalName().equals("person")) {
                        statement.setInt(1, id);
                        statement.setString(2, name);
                        statement.setInt(3, age);
                        statement.setString(4, city);
                        statement.executeUpdate();
                        
                        System.out.println("Data imported:  " + id);
                        System.out.println("Data imported:  " + name);
                        System.out.println("Data imported:  " + age);
                        System.out.println("Data imported:  " + city);
                    }
                    currentElement = "";
                }
            }

            xmlreader.close();
            System.out.println("Data imported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


