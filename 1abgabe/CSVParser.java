import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class CSVParser {

    String url = null;
    String name = null;
    String password = null;

    Connection con = null;

    public void parse(String filepath) {
        loadProperties();
        makeConnection();
        readCSV(filepath);
    }

    private void loadProperties() {
        // Load Properties
        Properties props = new Properties();
        String filepath = "1abgabe\\postgres.properties";

        try {
            FileInputStream fis = new FileInputStream(filepath);
            props.load(fis);
        } catch (IOException g) {
            System.out.println("Inputsream Exception (with properties file):" + g);
        }
        url = props.getProperty("url");
        name = props.getProperty("name");
        password = props.getProperty("password");
    }

    private void makeConnection() {
        try {
            con = DriverManager.getConnection(url, name, password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not connected!");
            }
        } catch (SQLException sqle) {
            System.out.println("SQLException occured when trying to connect to DB");
        }
    }

    private int checkForPerson(String name) {
        // does person already exist?
        // 0 = person does not exist
        // x = Person does exist with person_id = x
        int result = 0;
        String checkForPerson = "SELECT person_id FROM personen WHERE name = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkForPerson)) {
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("person_id");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return result;
    }

    private void parsePerson(int personid, String name) {
        String insert_person = "INSERT INTO personen (name) VALUES (?)";
        try {
            PreparedStatement statement = con.prepareStatement(insert_person);
            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void parseReview(String[] data, int person_id) {
        String insert_review = "INSERT INTO rezensionen (person_id, produkt_nr, date, summary, bewertung, content) VALUES (?, ?, ?, ?, ?, ?)";
        // String product = data[0];
        // int rating = data[1];
        // int helpful = data[2];
        // String reviewdate = data[3];
        // String user = data[4];
        // String summary = data[5];
        // String content = data[6];
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].replaceAll("^\"|\"$", ""); // remove leading and trailing quotes
        }

        try {
            PreparedStatement statement = con.prepareStatement(insert_review);
            short rating = Short.parseShort(data[1]);
            java.sql.Date date = java.sql.Date.valueOf(data[3]);
            statement.setInt(1, person_id); // person_id int
            statement.setString(2, data[0]); // produkt_nr varchar
            statement.setDate(3, date); // date date
            statement.setString(4, data[5]); // summary varchar
            statement.setShort(5, rating); // bewertung smallint
            statement.setString(6, data[6]); // conetent text
            statement.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    private void readCSV(String csvfilepath) {
        // Read csv
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(csvfilepath));

            // handle header
            String headerLine = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

                // String product = data[0];
                // int rating = data[1];
                // int helpful = data[2];
                // String reviewdate = data[3];
                // String user = data[4];
                // String summary = data[5];
                // String content = data[6];

                int personid = checkForPerson(data[4]);
                if (personid == 0) {
                    parsePerson(personid, data[4]);
                }

                parseReview(data, personid);

                // insert person

                // person exists
                // person does not exist

                // insert review
                // get person_id

            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

// try {
// PreparedStatement statement = con.prepareStatement(insert_review);
// int id = Integer.parseInt(data[0]);
// String names = data[1];
// int age = Integer.parseInt(data[2]);
// String city = data.length == 4 ? data[3] : "";
// statement.setInt(1, id);
// statement.setString(2, names);
// statement.setInt(3, age);
// statement.setString(4, city);
// statement.executeUpdate();

// } catch(SQLException sqle) {
// sqle.printStackTrace();
// }