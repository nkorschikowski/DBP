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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVParser {

    String url = null;
    String name = null;
    String password = null;

    Connection con = null;

    public void parse(String csvfilepath) {

        loadProperties();
        makeConnection();
        readCSV(csvfilepath);

        System.out.println("finished CSV Import");
    }

    private void loadProperties() {
        // Load Properties
        Properties props = new Properties();
        String filepath = "1abgabe\\postgres.properties";

        try {
            FileInputStream fis = new FileInputStream(filepath);
            props.load(fis);
        } catch (IOException ioe) {
            System.out.println("Inputsream Exception (with properties file):" + ioe);
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
            logdeny(data[0], sqle);
        }
    }

    private void readCSV(String csvfilepath) {
        // Read csv
        BufferedReader reader = null;
        String line = "";

        try {
            // handle header
            String headerLine = reader.readLine();

            while ((line = reader.readLine()) != null) {
                // erase first an last \"
                line = line.substring(1, line.length() - 1);

                String[] data = line.split("\",\"", -1);

                int personid = checkForPerson(data[4]);
                if (personid == 0) {
                    parsePerson(personid, data[4]);
                    // setzte personid auf die id der gerade geparsten Person
                    String checkForPerson = "SELECT person_id FROM personen WHERE name = ?";
                    try (PreparedStatement checkStmt = con.prepareStatement(checkForPerson)) {
                        checkStmt.setString(1, name);
                        ResultSet rs = checkStmt.executeQuery();
                        if (rs.next()) {
                            personid = rs.getInt("person_id");
                        }
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }

                parseReview(data, personid);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void logdeny(String produkt_nr, SQLException e) {
        try (FileWriter log = new FileWriter("CSVabgelehnt.txt", true)) {
            log.write("Failed to Insert review of Produkt \"" + produkt_nr + "\"\n");
            log.write("SQL Error: " + e.getMessage() + "\n ------------ \n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}