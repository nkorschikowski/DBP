
// kommentiertes Java-Ladeprogramm bzw. SQL Statements zur XML-Transformation
// soll automatisch inkonsistente Datensätze ablehnen und in "abgelehnt.txt"
// schreiben
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLparser {
    // Cache for table columns to avoid repeated DB queries
    private static final Map<String, Set<String>> columnCache = new HashMap<>();

    public void parse(String filepath) throws Exception {
        String xmlpath = filepath; // inputfile static

        // load server credentials
        Properties props = new Properties();
        String propfilepath = "1abgabe\\my.properties";
        try {
            FileInputStream fis = new FileInputStream(propfilepath);
            props.load(fis);
        } catch (IOException g) {
            System.out.println("Inputsream Excaption:" + g);
        }

        String url = props.getProperty("url");
        String name = props.getProperty("name");
        String password = props.getProperty("password");
        insertData(url, name, password, xmlpath);
        System.out.println("Data inserted to database");
    }

    public static void insertData(String url, String name, String password, String path) throws Exception {
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(url, name, password)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            InputStream inputStream = new FileInputStream(path); // <-- Data input
            Reader xmlReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            XMLEventReader reader = factory.createXMLEventReader(xmlReader);

            String currentTable = null;
            Map<String, String> rowData = new HashMap<>();

            // var for shop
            String shopName = null;
            String adresse = null;

            // var for table produkte
            String produkt_nr = null;
            String titel = null;
            String bild = null;
            String produkttyp = null;
            Float rating = 0.0F;
            int verkaufsrang = 0;

            // var for table buecher
            int seitenzahl = 0;
            LocalDate erscheinungsdatum = null;
            String isbn = null;
            String verlag = null;

            // var for dvds
            String format = null;
            LocalTime laufzeit = null;
            int region_code = 0;

            // var for musikcds
            String label = null;
            String songTitle = null;

            // var for personen
            String personenName = null;
            int personen_key = -1;

            // var dvd_personen
            String rolle = null;

            // var similars
            String produkt_nr1 = null;
            String produkt_nr2 = null;

            // SQL var
            String sql = null;
            PreparedStatement statement;

            // Read XML file using StAX parser
            while (reader.hasNext()) {
                int i = 0;
                i++;
                XMLEvent event = reader.nextEvent();
                XMLEvent nextEvent;
                StringBuilder sb;
                System.out.println("Reading in event:" + event);
                System.out.println();

                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    String tag = start.getName().getLocalPart();
                    Attribute attr;
                    System.out.println("Event is Startevent:" + start);
                    System.out.println();

                    switch (tag) {
                        case "shop":
                            attr = start.getAttributeByName(new QName("name"));
                            shopName = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("street"));
                            String str = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("zip"));
                            String zip = (attr != null) ? attr.getValue() : "";

                            adresse = str + ", " + zip;
                            System.out.println("Save var:" + shopName + ' ' + adresse);
                            System.out.println();
                            // sql befehl insert into adressen straße, hausnummer, zusatz, plz, stadt
                            sql = "INSERT INTO adressen (straße, hausnummer, zusatz, plz, stadt) VALUES (?, ?, ?, ?, ?)";
                            statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                            statement.setString(1, str);
                            statement.setInt(2, 1);
                            statement.setString(3, "");
                            statement.setInt(4, Integer.parseInt(zip));
                            statement.setString(5, shopName);
                            statement.executeUpdate();
                            ResultSet adressSet = statement.getGeneratedKeys();
                            int adressId = -1;
                            if (adressSet.next()) {
                                adressId = adressSet.getInt(1);
                            } else {
                                throw new SQLException("No address ID generated!");
                            }
                            // sql befehl inset into filialen name, adresse
                            sql = "INSERT INTO filialen (name, adress_id) VALUES (?, ?)";
                            statement = conn.prepareStatement(sql);
                            statement.setString(1, shopName);
                            statement.setInt(2, adressId);
                            statement.executeUpdate();
                            break;

                        case "item":
                            attr = start.getAttributeByName(new QName("pgroup"));
                            produkttyp = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("asin"));
                            produkt_nr = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("salesrank"));
                            verkaufsrang = -1;
                            if (attr != null) {
                                try {
                                    verkaufsrang = Integer.parseInt(attr.getValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid salesrank: " + attr.getValue());
                                }
                            }

                            attr = start.getAttributeByName(new QName("picture"));
                            bild = (attr != null) ? attr.getValue() : "";
                            titel = "";

                            // sql befehl insert into produkte (leipzig in titel) (dresden in details)
                            System.out.println("Save var: " + produkt_nr);
                            System.out.println("Save var: " + bild);
                            System.out.println("Save var: " + produkttyp);
                            System.out.println("Save var: " + verkaufsrang);
                            System.out.println();
                            break;

                        case "details":
                            attr = start.getAttributeByName(new QName("img"));
                            bild = (attr != null) ? attr.getValue() : "";
                            System.out.println("Save var:" + bild);
                            System.out.println();
                            // sqlbefehl insert into produkte titel, rating, verkaufsrang, bild, produkttyp
                            // dresden
                            if (shopName.equals("Dresden")) {
                                try {
                                    sql = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (?, ?, ?, ?, ?, ?)";
                                    statement = conn.prepareStatement(sql);
                                    statement.setString(1, produkt_nr);
                                    statement.setString(2, titel);
                                    statement.setFloat(3, rating);
                                    statement.setInt(4, verkaufsrang);

                                    if (bild != null) {
                                        statement.setString(5, bild);
                                    } else {
                                        statement.setNull(5, Types.VARCHAR);
                                    }

                                    statement.setString(6, produkttyp);
                                    statement.executeUpdate();

                                } catch (SQLException e) {
                                    try (FileWriter fw = new FileWriter("errors.txt", true);
                                            PrintWriter pw = new PrintWriter(fw)) {

                                        pw.println("Error inserting into produkte:");
                                        pw.println("produkt_nr: " + produkt_nr);
                                        pw.println("titel: " + titel);
                                        pw.println("rating: " + rating);
                                        pw.println("verkaufsrang: " + verkaufsrang);
                                        pw.println("bild: " + bild);
                                        pw.println("produkttyp: " + produkttyp);
                                        pw.println("SQLState: " + e.getSQLState());
                                        pw.println("Message: " + e.getMessage());
                                        pw.println("-----");

                                    } catch (IOException ioEx) {
                                        System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                    }
                                }

                            }
                            break;

                        case "title":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement()
                                    && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            titel = "";
                            titel = sb.toString().trim();
                            // sql befehl insert into produkte leipzig
                            if (shopName.equals("Leipzig")) {
                                try {
                                    sql = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (?, ?, ?, ?, ?, ?)";
                                    statement = conn.prepareStatement(sql);
                                    statement.setString(1, produkt_nr);
                                    statement.setString(2, titel);
                                    statement.setFloat(3, rating);
                                    statement.setInt(4, verkaufsrang);
                                    statement.setString(5, bild);
                                    statement.setString(6, produkttyp);
                                    statement.executeUpdate();
                                } catch (SQLException e) {
                                    if ("23505".equals(e.getSQLState())) { // Duplicate key violation
                                        try (FileWriter fw = new FileWriter("errors.txt", true);
                                                PrintWriter pw = new PrintWriter(fw)) {
                                            pw.println("Duplicate key error when inserting into produkte:");
                                            pw.println("produkt_nr: " + produkt_nr);
                                            pw.println("Error: " + e.getMessage());
                                            pw.println("-----");
                                        } catch (IOException ioEx) {
                                            System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                        }
                                    } else {
                                        throw e; // Re-throw if it's a different error
                                    }
                                }
                            }
                            System.out.println("Save var:" + titel);
                            System.out.println();
                            break;

                        case "tracks":
                            while (reader.hasNext()) {
                                event = reader.nextEvent();

                                // If a <title> tag is found
                                if (event.isStartElement()
                                        && event.asStartElement().getName().getLocalPart().equals("title")) {
                                    event = reader.nextEvent(); // get the character data
                                    if (event.isCharacters()) {
                                        String title = event.asCharacters().getData().trim();
                                        songTitle = title;

                                        try {
                                            // SQL insert for title
                                            sql = "INSERT INTO titel (name, produkt_nr) VALUES (?, ?)";
                                            statement = conn.prepareStatement(sql);
                                            statement.setString(1, songTitle);
                                            statement.setString(2, produkt_nr);
                                            statement.executeUpdate();

                                            System.out.println("Track title inserted: " + title);

                                        } catch (SQLException e) {
                                            // Log duplicate key or other errors to a file
                                            try (FileWriter fw = new FileWriter("errors.txt", true);
                                                    PrintWriter pw = new PrintWriter(fw)) {
                                                pw.println("Failed to insert track: \"" + songTitle + "\" for product "
                                                        + produkt_nr);
                                                pw.println("Error: " + e.getMessage());
                                                pw.println("---");
                                            } catch (IOException ioEx) {
                                                System.err
                                                        .println("Failed to write to error log: " + ioEx.getMessage());
                                            }
                                        }
                                    }
                                }

                                // If we reach the end of </tracks>, we break the loop
                                if (event.isEndElement()
                                        && event.asEndElement().getName().getLocalPart().equals("tracks")) {
                                    break;
                                }
                            }
                            break;

                        case "isbn":
                            attr = start.getAttributeByName(new QName("val"));
                            isbn = (attr != null) ? attr.getValue() : "";
                            System.out.println("Save var:" + isbn);
                            System.out.println();
                            break;

                        case "publication":
                            attr = start.getAttributeByName(new QName("date"));
                            try {
                                erscheinungsdatum = (attr != null && attr.getValue() != null
                                        && !attr.getValue().isEmpty())
                                                ? LocalDate.parse(attr.getValue())
                                                : null;
                            } catch (DateTimeParseException e) {
                                erscheinungsdatum = null;
                                System.err.println("Invalid date: " + attr.getValue());
                            }
                            if (tag.equals("Dresden")) {
                                // sqlbefhel insert into autoren_buecher produkt_nr, person_id
                                sql = "INSERT INTO autoren_buecher (produkt_nr, person_id) VALUES (?,?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setInt(2, personen_key);
                                statement.executeUpdate();
                            }
                            System.out.println("Save var:" + erscheinungsdatum);
                            System.out.println();
                            break;

                        case "releasedate":
                            if (titel.isEmpty()) {
                                try {
                                    sql = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (?, ?, ?, ?, ?, ?)";
                                    statement = conn.prepareStatement(sql);
                                    statement.setString(1, produkt_nr);
                                    statement.setString(2, titel);
                                    statement.setFloat(3, rating);
                                    statement.setInt(4, verkaufsrang);
                                    statement.setString(5, bild);
                                    statement.setString(6, produkttyp);
                                    statement.executeUpdate();
                                } catch (SQLException e) {
                                    if ("23505".equals(e.getSQLState())) { // Duplicate key violation
                                        try (FileWriter fw = new FileWriter("errors.txt", true);
                                                PrintWriter pw = new PrintWriter(fw)) {
                                            pw.println("Duplicate key error when inserting into produkte:");
                                            pw.println("produkt_nr: " + produkt_nr);
                                            pw.println("Error: " + e.getMessage());
                                            pw.println("-----");
                                        } catch (IOException ioEx) {
                                            System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                        }
                                    } else {
                                        throw e; // Re-throw if it's a different error
                                    }
                                }
                            }
                            try {
                                StringBuilder dateBuilder = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    dateBuilder.append(nextEvent.asCharacters().getData());
                                } else if (nextEvent.isEndElement()
                                        && nextEvent.asEndElement().getName().getLocalPart().equals("/releasedate")) {
                                    break;
                                }
                                if (!dateBuilder.isEmpty()) {
                                    erscheinungsdatum = LocalDate.parse(dateBuilder.toString().trim());
                                }
                                System.out.println("Save var:" + erscheinungsdatum);
                            } catch (Exception e) {
                                System.err.println("Invalid releasedate format.");
                            }
                            if (tag.equals("Dresden")) {
                                // sqlbefehl insert into kuenstler_cds produkt_nr, person_id
                                sql = "INSERT INTO kuenstler_cds (produkt_nr, person_id) VALUES (?,?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setInt(2, personen_key);
                                statement.executeUpdate();
                            }
                            System.out.println();
                            break;

                        case "format":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement()
                                    && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            format = sb.toString().trim();
                            System.out.println("Save var:" + format);
                            System.out.println();
                            break;

                        case "regioncode":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement()
                                    && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            if (!sb.isEmpty()) {
                                region_code = Integer.parseInt(sb.toString().trim());
                            }
                            System.out.println("Save var:" + region_code);
                            System.out.println();
                            break;

                        case "runningtime":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement()
                                    && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            if (sb != null) {
                                try {
                                    int minutes = Integer.parseInt(sb.toString().trim());
                                    laufzeit = LocalTime.of(minutes / 60, minutes % 60);

                                    System.out.println("Save var:" + laufzeit);
                                } catch (Exception e) {
                                    System.err.println("Invalid time: " + sb.toString().trim());
                                }
                            }
                            if (titel.isEmpty()) {
                                try {
                                    sql = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (?, ?, ?, ?, ?, ?)";
                                    statement = conn.prepareStatement(sql);
                                    statement.setString(1, produkt_nr);
                                    statement.setString(2, titel);
                                    statement.setFloat(3, rating);
                                    statement.setInt(4, verkaufsrang);
                                    statement.setString(5, bild);
                                    statement.setString(6, produkttyp);
                                    statement.executeUpdate();
                                } catch (SQLException e) {
                                    if ("23505".equals(e.getSQLState())) { // Duplicate key violation
                                        try (FileWriter fw = new FileWriter("errors.txt", true);
                                                PrintWriter pw = new PrintWriter(fw)) {
                                            pw.println("Duplicate key error when inserting into produkte:");
                                            pw.println("produkt_nr: " + produkt_nr);
                                            pw.println("Error: " + e.getMessage());
                                            pw.println("-----");
                                        } catch (IOException ioEx) {
                                            System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                        }
                                    } else {
                                        throw e; // Re-throw if it's a different error
                                    }
                                }
                            }
                            // insert into dvds format, laufzeit, regioncode
                            try {
                                sql = "INSERT INTO dvds (produkt_nr, format, laufzeit, region_code) VALUES (?, ?, ?, ?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setString(2, songTitle);
                                statement.setTime(3, Time.valueOf(laufzeit));
                                statement.setInt(4, region_code);
                                statement.executeUpdate();
                                System.out.println("Insert successful.");
                            } catch (SQLIntegrityConstraintViolationException e) {
                                // Handles duplicate key or other constraint violations
                                System.err.println("Error: Duplicate key or integrity constraint violation.");
                                e.printStackTrace();
                            } catch (SQLException e) {
                                // Handles other SQL-related errors
                                System.err.println("SQL Error occurred:");
                                e.printStackTrace();
                            }

                            if (tag.equals("Dresden")) {
                                // sqlbefehl insert into kuenstler_cds produkt_nr, person_id
                                sql = "INSERT INTO dvd_personen (produkt_nr, person_idn, rolle) VALUES (?, ?, ?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setInt(2, personen_key);
                                statement.setString(3, rolle);
                                statement.executeUpdate();
                            }
                            break;

                        case "publisher":
                            if (shopName.equals("Leipzig")) {
                                attr = start.getAttributeByName(new QName("name"));
                                verlag = (attr != null) ? attr.getValue() : "";
                                System.out.println("Save var:" + verlag);
                                System.out.println();

                            } else if (shopName.equals("Dresden")) {
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                } else if (nextEvent.isEndElement()
                                        && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                    break;
                                }

                                verlag = sb.toString().trim();
                                System.out.println("Save var:" + verlag);
                                System.out.println();
                            }

                            // sql befehl insert into buecher seitenzahl, erscheinungsdatum, isbn, verlag
                            try {
                                sql = "INSERT INTO buecher (produkt_nr, seitenzahl, erscheinungsdatum, isbn, verlag) VALUES (?, ?, ?, ?, ?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setInt(2, seitenzahl);

                                if (erscheinungsdatum != null) {
                                    statement.setDate(3, Date.valueOf(erscheinungsdatum));
                                } else {
                                    statement.setNull(3, Types.DATE);
                                }

                                statement.setString(4, isbn);
                                statement.setString(5, verlag);
                                statement.executeUpdate();

                            } catch (SQLException e) {
                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                        PrintWriter pw = new PrintWriter(fw)) {

                                    if ("23505".equals(e.getSQLState())) { // Duplicate key
                                        pw.println("Duplicate key for buch_id: " + produkt_nr);
                                    } else if ("23503".equals(e.getSQLState())) { // Foreign key violation
                                        pw.println("Missing produkt_nr in produkte table for buch_id: " + produkt_nr);
                                    } else {
                                        pw.println("Unexpected SQL error for buch_id: " + produkt_nr);
                                    }

                                    pw.println("SQLState: " + e.getSQLState());
                                    pw.println("Message: " + e.getMessage());
                                    pw.println("---");

                                } catch (IOException ioEx) {
                                    System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                }
                            }

                            break;

                        case "label":
                            if (shopName.equals("Leipzig")) {
                                attr = start.getAttributeByName(new QName("name"));
                                label = (attr != null) ? attr.getValue() : "";
                                System.out.println("Save var:" + label);
                                System.out.println();

                            } else if (shopName.equals("Dresden")) {
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                } else if (nextEvent.isEndElement()
                                        && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                    break;
                                }
                                label = sb.toString().trim();
                                System.out.println("Save var:" + label);
                                System.out.println();

                            }
                            // sql befehl insert into musikcds label, erscheinungsdatum
                            try {
                                sql = "INSERT INTO musikcds (produkt_nr, label, erscheinungsdatum) VALUES (?, ?, ?)";
                                statement = conn.prepareStatement(sql);
                                statement.setString(1, produkt_nr);
                                statement.setString(2, label);

                                if (erscheinungsdatum != null) {
                                    statement.setDate(3, Date.valueOf(erscheinungsdatum));
                                } else {
                                    statement.setNull(3, Types.DATE);
                                }

                                statement.executeUpdate();

                            } catch (SQLException e) {
                                if ("23505".equals(e.getSQLState())) { // 23505 = unique_violation
                                    try (FileWriter fw = new FileWriter("errors.txt", true);
                                            PrintWriter pw = new PrintWriter(fw)) {
                                        pw.println("Duplicate key error when inserting into musikcds:");
                                        pw.println("produkt_nr: " + produkt_nr);
                                        pw.println("Error: " + e.getMessage());
                                        pw.println("-----");
                                    } catch (IOException ioEx) {
                                        System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                    }
                                } else {
                                    throw e; // rethrow other SQL errors
                                }
                            }

                            break;

                        case "similars":
                            produkt_nr1 = produkt_nr;
                            if (shopName.equals("Leipzig")) {
                                while (reader.hasNext()) {
                                    event = reader.nextEvent();

                                    // Start of <sim_product>
                                    if (event.isStartElement()
                                            && event.asStartElement().getName().getLocalPart().equals("sim_product")) {
                                        produkt_nr2 = null;

                                        while (reader.hasNext()) {
                                            event = reader.nextEvent();

                                            // Detect <asin> start
                                            if (event.isStartElement()
                                                    && event.asStartElement().getName().getLocalPart().equals("asin")) {
                                                event = reader.nextEvent();
                                                if (event.isCharacters()) {
                                                    produkt_nr2 = event.asCharacters().getData().trim();
                                                    // sql befehl insert into aehnliche_produkte produkt_nr1,
                                                    // produkt_nr2
                                                    try {
                                                        sql = "INSERT INTO aehnliche_produkte (produkt_nr1, produkt_nr2) VALUES (?, ?)";
                                                        statement = conn.prepareStatement(sql);
                                                        statement.setString(1, produkt_nr1);
                                                        statement.setString(2, produkt_nr2);
                                                        statement.executeUpdate();
                                                    } catch (SQLException e) {
                                                        String sqlState = e.getSQLState();

                                                        try (FileWriter fw = new FileWriter("errors.txt", true);
                                                                PrintWriter pw = new PrintWriter(fw)) {

                                                            if ("23503".equals(sqlState)) { // Foreign key violation
                                                                pw.println("Foreign Key Violation:");
                                                                pw.println("produkt_nr1: " + produkt_nr1
                                                                        + ", produkt_nr2: " + produkt_nr2);
                                                            } else if ("23505".equals(sqlState)) { // Duplicate key
                                                                pw.println("Duplicate Key Violation:");
                                                                pw.println("produkt_nr1: " + produkt_nr1
                                                                        + ", produkt_nr2: " + produkt_nr2);
                                                            } else {
                                                                // Unknown SQL error, rethrow
                                                                throw e;
                                                            }

                                                            pw.println("Error Message: " + e.getMessage());
                                                            pw.println("---");

                                                        } catch (IOException ioEx) {
                                                            System.err.println("Could not write to errors.txt: "
                                                                    + ioEx.getMessage());
                                                        }
                                                    }

                                                    System.out.println("Save var: " + produkt_nr2);
                                                }
                                            }

                                            // Break out when we reach </sim_product>
                                            if (event.isEndElement() && event.asEndElement().getName().getLocalPart()
                                                    .equals("sim_product")) {
                                                break;
                                            }
                                        }
                                    }

                                    // Stop when </similars> is reached
                                    if (event.isEndElement()
                                            && event.asEndElement().getName().getLocalPart().equals("similars")) {
                                        break;
                                    }
                                }

                            } else if (shopName.equals("Dresden")) {
                                while (reader.hasNext()) {
                                    event = reader.nextEvent();

                                    // Exit if we reach the end of <similars>
                                    if (event.isEndElement()
                                            && event.asEndElement().getName().getLocalPart().equals("similars")) {
                                        break;
                                    }

                                    // Only process start elements
                                    if (event.isStartElement()) {
                                        start = event.asStartElement();
                                        tag = start.getName().getLocalPart();

                                        if ("item".equals(tag)) {
                                            attr = start.getAttributeByName(new QName("asin"));
                                            produkt_nr2 = (attr != null) ? attr.getValue() : "";
                                            // insert into aehnliche_produkte produkt_nr1, produkt_nr2
                                            try {
                                                sql = "INSERT INTO aehnliche_produkte (produkt_nr1, produkt_nr2) VALUES (?, ?)";
                                                statement = conn.prepareStatement(sql);
                                                statement.setString(1, produkt_nr1);
                                                statement.setString(2, produkt_nr2);
                                                statement.executeUpdate();

                                            } catch (SQLException e) {
                                                String errorType;
                                                if ("23503".equals(e.getSQLState())) {
                                                    errorType = "Foreign key violation";
                                                } else if ("23505".equals(e.getSQLState())) {
                                                    errorType = "Duplicate key violation";
                                                } else {
                                                    throw e; // rethrow other SQL errors
                                                }

                                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                                        PrintWriter pw = new PrintWriter(fw)) {
                                                    pw.println(errorType + " while inserting into aehnliche_produkte:");
                                                    pw.println("produkt_nr1: " + produkt_nr1);
                                                    pw.println("produkt_nr2: " + produkt_nr2);
                                                    pw.println("Error: " + e.getMessage());
                                                    pw.println("---");
                                                } catch (IOException ioEx) {
                                                    System.err.println(
                                                            "Could not write to errors.txt: " + ioEx.getMessage());
                                                }
                                            }

                                            System.out.println("Save var:" + produkt_nr1);
                                            System.out.println("Save var:" + produkt_nr2);
                                            System.out.println();

                                        }
                                    }
                                }
                                break;
                            }
                            break;

                        case "author":
                        case "artist":
                        case "actor":
                        case "creator":
                        case "director":
                            if (shopName.equals("Leipzig")) {
                                attr = start.getAttributeByName(new QName("name"));
                                personenName = (attr != null) ? attr.getValue() : "";

                                System.out.println("Save var:" + personenName);
                                System.out.println();

                            } else if (shopName.equals("Dresden")) {
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                    personenName = sb.toString().trim();

                                } else if (nextEvent.isEndElement() &&
                                        nextEvent.asEndElement().getName().getLocalPart().equals("/author") ||
                                        nextEvent.asEndElement().getName().getLocalPart().equals("/artist") ||
                                        nextEvent.asEndElement().getName().getLocalPart().equals("/actor") ||
                                        nextEvent.asEndElement().getName().getLocalPart().equals("/creator") ||
                                        nextEvent.asEndElement().getName().getLocalPart().equals("/director")) {
                                    break;
                                }
                            }
                            // sql befehl insert into personen person_id, name
                            sql = "INSERT INTO personen (name) VALUES (?)";
                            statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                            statement.setString(1, personenName);
                            statement.executeUpdate();
                            ResultSet personSet = statement.getGeneratedKeys();

                            if (personSet.next()) {
                                personen_key = personSet.getInt(1);
                            } else {
                                throw new SQLException("No address ID generated!");
                            }
                            switch (tag) {
                                case "actor":
                                    rolle = "Actor";
                                    if (shopName.equals("Leipzig")) {
                                        try {
                                            sql = "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (?, ?, ?)";
                                            statement = conn.prepareStatement(sql);
                                            statement.setString(1, produkt_nr);
                                            statement.setInt(2, personen_key);
                                            statement.setString(3, rolle);
                                            statement.executeUpdate();
                                        } catch (SQLException e) {
                                            if (e.getSQLState().equals("23503")) { // Foreign key violation
                                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                                        PrintWriter pw = new PrintWriter(fw)) {
                                                    pw.println("Foreign key constraint failed for produkt_nr: "
                                                            + produkt_nr);
                                                    pw.println("Error: " + e.getMessage());
                                                    pw.println("---");
                                                }
                                            } else {
                                                throw e; // re-throw other unexpected SQL errors
                                            }
                                        }
                                    }
                                    break;
                                case "creator":
                                    rolle = "Producer";
                                    if (shopName.equals("Leipzig")) {
                                        try {
                                            sql = "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (?, ?, ?)";
                                            statement = conn.prepareStatement(sql);
                                            statement.setString(1, produkt_nr);
                                            statement.setInt(2, personen_key);
                                            statement.setString(3, rolle);
                                            statement.executeUpdate();
                                        } catch (SQLException e) {
                                            if (e.getSQLState().equals("23503")) { // Foreign key violation
                                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                                        PrintWriter pw = new PrintWriter(fw)) {
                                                    pw.println("Foreign key constraint failed for produkt_nr: "
                                                            + produkt_nr);
                                                    pw.println("Error: " + e.getMessage());
                                                    pw.println("---");
                                                }
                                            } else {
                                                throw e; // re-throw other unexpected SQL errors
                                            }
                                        }
                                    }
                                    break;
                                case "director":
                                    rolle = "Director";
                                    if (shopName.equals("Leipzig")) {
                                        try {
                                            sql = "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (?, ?, ?)";
                                            statement = conn.prepareStatement(sql);
                                            statement.setString(1, produkt_nr);
                                            statement.setInt(2, personen_key);
                                            statement.setString(3, rolle);
                                            statement.executeUpdate();
                                        } catch (SQLException e) {
                                            if (e.getSQLState().equals("23503")) { // Foreign key violation
                                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                                        PrintWriter pw = new PrintWriter(fw)) {
                                                    pw.println("Foreign key constraint failed for produkt_nr: "
                                                            + produkt_nr);
                                                    pw.println("Error: " + e.getMessage());
                                                    pw.println("---");
                                                }
                                            } else {
                                                throw e; // re-throw other unexpected SQL errors
                                            }
                                        }
                                    }
                                    break;
                                case "artist":
                                    if (shopName.equals("Leipzig")) {
                                        try {
                                            sql = "INSERT INTO musikcds (produkt_nr, label, erscheinungsdatum) VALUES (?, ?, ?)";
                                            statement = conn.prepareStatement(sql);
                                            statement.setString(1, produkt_nr);
                                            statement.setString(2, label);

                                            if (erscheinungsdatum != null) {
                                                statement.setDate(3, Date.valueOf(erscheinungsdatum));
                                            } else {
                                                statement.setNull(3, Types.DATE);
                                            }

                                            statement.executeUpdate();

                                        } catch (SQLException e) {
                                            if ("23505".equals(e.getSQLState())) { // 23505 = unique_violation
                                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                                        PrintWriter pw = new PrintWriter(fw)) {
                                                    pw.println("Duplicate key error when inserting into musikcds:");
                                                    pw.println("produkt_nr: " + produkt_nr);
                                                    pw.println("Error: " + e.getMessage());
                                                    pw.println("-----");
                                                } catch (IOException ioEx) {
                                                    System.err.println(
                                                            "Could not write to errors.txt: " + ioEx.getMessage());
                                                }
                                            } else {
                                                throw e; // rethrow other SQL errors
                                            }
                                        }

                                        // sqlbefehl insert into kuenstler_cds produkt_nr, person_id
                                        sql = "INSERT INTO kuenstler_cds (produkt_nr, person_id) VALUES (?,?)";
                                        statement = conn.prepareStatement(sql);
                                        statement.setString(1, produkt_nr);
                                        statement.setInt(2, personen_key);
                                        statement.executeUpdate();
                                    }
                                    break;
                                case "author":
                                    if (shopName.equals("Leipzig")) {
                                        // sqlbefhel insert into autoren_buecher produkt_nr, person_id
                                        sql = "INSERT INTO autoren_buecher (produkt_nr, person_id) VALUES (?,?)";
                                        statement = conn.prepareStatement(sql);
                                        statement.setString(1, produkt_nr);
                                        statement.setInt(2, personen_key);
                                        statement.executeUpdate();
                                    }
                                    break;
                                default:
                                    rolle = "";
                            }

                            System.out.println("Save var:" + personenName);
                            System.out.println("Save var:" + rolle);
                            System.out.println();
                            break;
                    }

                    /*
                     * Programmcode für tabellennamen und tags identisch. Da nein case ansatz
                     * gewählt
                     * if (isTable(conn,tag)) {
                     * currentTable = tag;
                     * System.out.println("Event is tag:" + tag);
                     * System.out.println();
                     * rowData.clear();
                     * } else if (currentTable != null) {
                     * event = reader.nextEvent();
                     * if (event.isCharacters()) {
                     * rowData.put(tag, event.asCharacters().getData().trim());
                     * System.out.println("Event is Character: " +
                     * event.asCharacters().getData().trim());
                     * }
                     * }
                     */

                } else if (event.isEndElement()) {
                    EndElement end = event.asEndElement();
                    String tag = end.getName().getLocalPart();
                    System.out.println("Event is Endevent:" + tag);
                    System.out.println(":___________________:");
                    if (tag.equals(currentTable)) {
                        insertRow(conn, currentTable, rowData);
                        currentTable = null;
                        rowData.clear();
                    }
                }
            }
        }
    }

    // Check if a tag represents a table name
    private static boolean isTable(Connection conn, String tag) throws SQLException {
        /*
         * return Set.of("produkte", "buecher", "dvds", "musikcds", "titel", "personen",
         * "autoren_buecher", "kuenstler_cds", "dvd_personen", "kategorien",
         * "unterkategorie", "produkt_kategorie", "aehnliche_produkte", "filialen",
         * "adressen", "angebote", "kunden", "kauf", "kauf_produkt",
         * "rezensionen").contains(tag);
         */
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
        Set<String> tables = new HashSet<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
        }
        System.out.println("Tage belongs to: " + tag + tables.contains(tag));
        return tables.contains(tag);
    }

    // Retrieve column names for a given table from the database
    private static Set<String> getTableColumns(Connection conn, String tableName) throws SQLException {
        if (columnCache.containsKey(tableName)) {
            return columnCache.get(tableName);
        }

        Set<String> columns = new HashSet<>();
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tableName.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                columns.add(rs.getString("column_name"));
            }
        }
        columnCache.put(tableName, columns);
        System.out.println("Colums needed for insert: " + columns);
        System.out.println("");
        return columns;
    }

    // Ensure person name exists in personen table and return person_id
    private static Integer getPersonId(Connection conn, String name) throws SQLException {
        if (name == null || name.isEmpty())
            return null;
        String query = "SELECT person_id FROM personen WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt("person_id");
        }

        // Insert person if not found
        String insert = "INSERT INTO personen(name) VALUES (?) RETURNING person_id";
        try (PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt("person_id");
        }
        return null;
    }

    // Dynamically build and execute INSERT statement based on valid table columns
    private static void insertRow(Connection conn, String tableName, Map<String, String> data) throws SQLException {
        Set<String> validColumns = getTableColumns(conn, tableName);
        Map<String, String> filteredData = new LinkedHashMap<>();

        // Filter and preprocess input data
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!validColumns.contains(key))
                continue;

            if (key.equals("person_id")) {
                Integer id = getPersonId(conn, value);
                if (id != null) {
                    filteredData.put(key, String.valueOf(id));
                }
            } else {
                filteredData.put(key, value);
            }
        }

        if (filteredData.isEmpty())
            return;

        // Construct SQL statement
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder placeholders = new StringBuilder(" VALUES (");
        List<String> columns = new ArrayList<>(filteredData.keySet());

        for (String col : columns) {
            sql.append(col).append(", ");
            placeholders.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        placeholders.setLength(placeholders.length() - 2);
        sql.append(")").append(placeholders).append(")");

        // Prepare and execute statement
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < columns.size(); i++) {
                setTypedValue(stmt, i + 1, filteredData.get(columns.get(i)));
            }
            stmt.executeUpdate();
        }
    }

    // Convert string values to appropriate SQL types
    private static void setTypedValue(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value.matches("\\d+")) {
            stmt.setInt(index, Integer.parseInt(value));
        } else if (value.matches("\\d+\\.\\d+")) {
            stmt.setFloat(index, Float.parseFloat(value));
        } else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            stmt.setDate(index, Date.valueOf(value));
        } else {
            stmt.setString(index, value);
        }
    }
}
