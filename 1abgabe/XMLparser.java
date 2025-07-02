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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

class XMLparser {

    // Cache for table columns to avoid repeated DB queries

    public void parse(String xmlfilepath) throws Exception {
        // load server credentials
        Properties props = new Properties();
        String propfilepath = "my.properties";
        try {
            FileInputStream fis = new FileInputStream(propfilepath);
            props.load(fis);
        } catch (IOException g) {
            System.out.println("Inputsream Excaption:" + g);
        }

        String url = props.getProperty("url");
        String name = props.getProperty("name");
        String password = props.getProperty("password");
        insertData(url, name, password, xmlfilepath);
        System.out.println("Data inserted to database");
    }

    public static void insertData(
        String url,
        String name, 
        String password, 
        String path
        )throws Exception 
    {
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(url, name, password)) 
        {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            InputStream inputStream = new FileInputStream(path); // <-- Data input
            Reader xmlReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            XMLEventReader reader = factory.createXMLEventReader(xmlReader);

            //var for shop
            String shopName = null;
            String adresse = null;
            int filiale_id = 0;
            // var for adress
            int adressId = 0;
            String str = "";
            String zip = "";
            String hausnummer = "";
            String zusatz = "";

            // var for table produkte 
            String produkt_nr = null; 
            String titel = null; 
            String bild = null;
            String produkttyp = null;
            Float rating = 0.0F;
            int verkaufsrang = 0;

            // var for price
            int preis;
            Float mult = 0.0f;
            String zustand = null;
            String currency;

            // var for table buecher
            int seitenzahl = 0;
            LocalDate erscheinungsdatum = null;
            String isbn = null;
            List<String> verlag = new ArrayList<>();
            
            // var for dvds
            String format = null;
            LocalTime laufzeit = null;
            int region_code = 0;

            // var for musikcds
            List<String> label = new ArrayList<>();
            String songTitle = null;
            List<String> song_list = new ArrayList<>();
            
            //var for personen
            List<String> personenName = new ArrayList<>();
            List<Integer> person_ids = new ArrayList<>();
            int personen_key= -1;


            // var dvd_personen
            List<String> rolle = new ArrayList<>();

            // var similars
            String produkt_nr1=null;
            String produkt_nr2=null;
            List<String> sim_list = new ArrayList<>();


            //SQL var
            String sql = null;
            PreparedStatement statement;

            int itemcount=0;
            FileWriter writer = new FileWriter("log.txt", true);

            // Read XML file using StAX parser
            while (reader.hasNext()) {
                int i = 0;
                i++;
                XMLEvent event = reader.nextEvent();
                XMLEvent nextEvent;
                StringBuilder sb;
                
                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    String tag = start.getName().getLocalPart();
                    Attribute attr;

                    switch(tag){
                        case "shop":
                            attr = start.getAttributeByName(new QName("name"));
                            shopName = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("street"));
                            str = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("zip"));
                            zip = (attr != null) ? attr.getValue() : "";

                            adresse = str + ", " + zip;

                            //insert sql befehle
                            //save atomatically generated adressId while inserting
                            //sql befehl insert into adressen straße, hausnummer, zusatz, plz, stadt
                            //sql befehl insert into shop name, adresse
                            adressId = insertAdress(conn, str, hausnummer, zusatz, zip, shopName);
                            filiale_id = insertShop(conn, shopName, adressId);
                            writer.write("\n" +"Save var:" + shopName + ' ' + adresse);
                            writer.write("\n" +"Save var:" + str + ' ' + hausnummer + ' ' + zip + ' ' + shopName); 
                            
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
                            break;

                        case "price":
                            
                            attr = start.getAttributeByName(new QName("mult"));
                            mult = Float.parseFloat((attr != null) ? attr.getValue() : "");

                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }

                            try {
                                String preisString = sb.toString().trim();
                                if (preisString.isEmpty()) {
                                    throw new NumberFormatException("Empty price value");
                                }
                                preis = Integer.parseInt(preisString);
                            } catch (NumberFormatException e) {
                                try (FileWriter fw = new FileWriter("errors.txt", true);
                                    PrintWriter pw = new PrintWriter(fw)) {
                                    pw.println("Error parsing price:");
                                    pw.println("Input: '" + sb.toString().trim() + "'");
                                    pw.println("Error: " + e.getMessage());
                                    pw.println("-----");
                                } catch (IOException ioEx) {
                                    System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                                }

                                // Handle fallback, e.g., default to 0 or skip record
                                preis = 0; // or: skip inserting this item
                            }
                            mult *= preis;
                            
                            attr = start.getAttributeByName(new QName("currency"));
                            currency = (attr != null) ? attr.getValue() : "";

                            attr = start.getAttributeByName(new QName("state"));
                            zustand = (attr != null) ? attr.getValue() : "";
                            break;

                        case "details":
                            attr = start.getAttributeByName(new QName("img"));
                            bild = (attr != null) ? attr.getValue() : "";
                            break;

                        case "title":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            titel = "";
                            titel = sb.toString().trim();
                            break;

                        case "tracks":
                            while (reader.hasNext()) {
                                event = reader.nextEvent();
                                // If a <title> tag is found
                                if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("title")) {
                                    event = reader.nextEvent(); // get the character data
                                    if (event.isCharacters()) {
                                        String title = event.asCharacters().getData().trim();
                                        songTitle = title;
                                        song_list.add(songTitle);
                                    }
                                }
                                    // If we reach the end of </tracks>, we break the loop
                                    if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("tracks")) {
                                        break;
                                    }
                                }
                                break;


                        case "isbn":
                            attr = start.getAttributeByName(new QName("val"));
                            isbn = (attr != null) ? attr.getValue() : "";
                            break;

                        case "pages":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();

                            if (nextEvent.isCharacters()) {
                                String data = nextEvent.asCharacters().getData().trim();
                                if (!data.isEmpty()) {
                                    seitenzahl = Integer.parseInt(data);
                                } else {
                                    seitenzahl = 0; // or any default value you'd like
                                }
                            } else if (nextEvent.isEndElement() &&
                                    nextEvent.asEndElement().getName().getLocalPart().equals("pages")) {
                                seitenzahl = 0; // empty element
                            }
                            break;
                            
                        case "publication":
                            attr = start.getAttributeByName(new QName("date"));
                            try {
                                erscheinungsdatum = (attr != null && attr.getValue() != null && !attr.getValue().isEmpty())
                                    ? LocalDate.parse(attr.getValue())
                                    : null;
                            } catch (DateTimeParseException e) {
                                erscheinungsdatum = null;
                                System.err.println("Invalid date: " + attr.getValue());
                            }
                            break;

                        case "releasedate": 
                             try {
                                StringBuilder dateBuilder = new StringBuilder();
                                    nextEvent = reader.nextEvent();
                                    if (nextEvent.isCharacters()) {
                                        dateBuilder.append(nextEvent.asCharacters().getData());
                                    } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/releasedate")) {
                                        break;
                                    }
                                if(!dateBuilder.isEmpty()){
                                erscheinungsdatum = LocalDate.parse(dateBuilder.toString().trim()); 
                                }
                            } catch (Exception e) {
                                System.err.println("Invalid releasedate format.");
                            }
                            break;

                        case "format":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            format = sb.toString().trim();
                            break;

                        case "regioncode":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            if(!sb.isEmpty()){
                                region_code = Integer.parseInt(sb.toString().trim());
                            }
                            break;

                        case "runningtime":
                            sb = new StringBuilder();
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isCharacters()) {
                                sb.append(nextEvent.asCharacters().getData());
                            } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                break;
                            }
                            if (sb != null) {
                                try {
                                    int minutes = Integer.parseInt(sb.toString().trim());
                                    laufzeit = LocalTime.of(minutes / 60, minutes % 60);
                                } catch (Exception e) {
                                    System.err.println("Invalid time: " + sb.toString().trim());
                                }
                            }
                            break;

                        case "publisher":
                            if(shopName.equals("Leipzig")){
                                attr = start.getAttributeByName(new QName("name"));
                                verlag.add((attr != null) ? attr.getValue() : "");
                            } else if (shopName.equals("Dresden")){
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                 break;
                                }
                                verlag.add(sb.toString().trim());
                            }
                            break;

                        case "label":
                            if(shopName.equals("Leipzig")){
                                attr = start.getAttributeByName(new QName("name"));
                                label.add((attr != null) ? attr.getValue() : "");
                                
                            } else if(shopName.equals( "Dresden")){ 
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                } else if (nextEvent.isEndElement() && nextEvent.asEndElement().getName().getLocalPart().equals("/titel")) {
                                    break;
                                }
                                label.add(sb.toString().trim());
                            }
                            break;

                        case "similars":
                        produkt_nr1 = produkt_nr;
                            if (shopName.equals("Leipzig")) {
                                while (reader.hasNext()) {
                                    event = reader.nextEvent();
                                    // Start of <sim_product>
                                    if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("sim_product")) {
                                        produkt_nr2 = null;
                                        while (reader.hasNext()) {
                                            event = reader.nextEvent();
                                            // Detect <asin> start
                                            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("asin")) {
                                                event = reader.nextEvent();
                                                if (event.isCharacters()) {
                                                    produkt_nr2 = event.asCharacters().getData().trim();
                                                    sim_list.add(produkt_nr2);
                                                }
                                            }
                                            // Break out when we reach </sim_product>
                                            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("sim_product")) {
                                                break;
                                            }
                                        }
                                    }

                                    // Stop when </similars> is reached
                                    if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("similars")) {
                                        break;
                                    }
                                }
                            }else if(shopName.equals("Dresden")){
                            while (reader.hasNext()) {
                                event = reader.nextEvent();
                                // Exit if we reach the end of <similars>
                                if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("similars")) {
                                    break;
                                }
                                // Only process start elements
                                if (event.isStartElement()) {
                                    start = event.asStartElement();
                                    tag = start.getName().getLocalPart();
                                    if ("item".equals(tag)) {
                                        attr = start.getAttributeByName(new QName("asin"));
                                        produkt_nr2 = (attr != null) ? attr.getValue() : "";
                                        sim_list.add(produkt_nr2);
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
                            if(shopName.equals("Leipzig")){
                                attr = start.getAttributeByName(new QName("name"));
                                personenName.add((attr != null) ? attr.getValue() : "");
                                
                            } else if(shopName.equals("Dresden")){
                                sb = new StringBuilder();
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isCharacters()) {
                                    sb.append(nextEvent.asCharacters().getData());
                                    personenName.add(sb.toString().trim());
                                } else if (nextEvent.isEndElement() && 
                                nextEvent.asEndElement().getName().getLocalPart().equals("/author") || 
                                nextEvent.asEndElement().getName().getLocalPart().equals("/artist") ||
                                nextEvent.asEndElement().getName().getLocalPart().equals("/actor")  ||
                                nextEvent.asEndElement().getName().getLocalPart().equals("/creator")||
                                nextEvent.asEndElement().getName().getLocalPart().equals("/director") ) {
                                    break;
                                }
                            }

                            switch(tag) {
                                case "actor": rolle.add("Actor");
                                    break;
                                case "creator": rolle.add("Producer");
                                    break;
                                case "director": rolle.add("Director");
                                    break;
                                case "artist": 
                                        break;
                                case "author": 
                                    break;
                                default:  
                                    break;
                            }
                            break;
                    }
                } else if (event.isEndElement()) {
                    EndElement end = event.asEndElement();
                    String tag = end.getName().getLocalPart();
                    if (tag.equals("item")) {
                        // insert items
                        //sqlbefehl insert into produkte titel, rating, verkaufsrang, bild, produkttyp dresden
                        insertItem(conn, produkt_nr, titel, rating, verkaufsrang, bild, produkttyp);
                        writer.write("\n" +"Save var: " + produkt_nr);
                        writer.write("\n" +"Save var: " + titel);
                        writer.write("\n" +"Save var: " + verkaufsrang);
                        writer.write("\n" +"Save var: " + bild);
                        writer.write("\n" +"Save var: " + produkttyp);
                        if(mult > 0){
                            insertAngebot(conn, produkt_nr, filiale_id , mult, zustand);
                            writer.write("\n" +"Save var: " + titel);
                            writer.write("\n" +"Save var: " + verkaufsrang);
                            writer.write("\n" +"Save var: " + bild);
                            writer.write("\n" +"Save var: " + produkttyp);
                             
                           
                        }
                        // insert person
                        for(String x : personenName){
                            //insert into Personen conn, name
                            person_ids.add(insertPerson(conn, x));
                            writer.write("\n" +"Save insertPerson:" + x);
                            
                            
                        }
                        // insert type dependent items
                        switch(produkttyp){
                            case "DVD":
                                //insert into dvds format, laufzeit, regioncode
                                insertFilmDvd(conn, produkt_nr, format, laufzeit, region_code);
                                writer.write("\n" +"Save insertFilmDvd: " + produkt_nr);
                                writer.write("\n" +"Save insertFilmDvd: " + format);
                                writer.write("\n" +"Save insertFilmDvd: " + laufzeit);
                                writer.write("\n" +"Save insertFilmDvd: " + region_code);
                                 
                                for(int k = 0; k<person_ids.size(); k++){
                                    //sqlbefehl insert into kuenstler_cds produkt_nr, person_id
                                    insertDvdPerson(conn, produkt_nr, person_ids.get(k), rolle.get(k));
                                    writer.write("\n" +"Save insertDvdPerson: " + produkt_nr);
                                    writer.write("\n" +"Save insertDvdPerson: " + person_ids.get(k));
                                    writer.write("\n" +"Save insertDvdPerson: " + rolle.get(k));
                                    
                                    
                                }
                                break;
                            case "Book":
                                for(String x : verlag){
                                    //sql befehl insert into buecher seitenzahl, erscheinungsdatum, isbn, verlag
                                    insertLiteraturBuch(conn, produkt_nr, seitenzahl, erscheinungsdatum, isbn, x);
                                    writer.write("\n" +"Save insertLiteraturBuch: " + produkt_nr);
                                    writer.write("\n" +"Save insertLiteraturBuch: " + seitenzahl);
                                    writer.write("\n" +"Save insertLiteraturBuch: " + erscheinungsdatum);
                                    writer.write("\n" +"Save insertLiteraturBuch: " + isbn);
                                    writer.write("\n" +"Save insertLiteraturBuch: " + x);
                                    
                                }
                                for(int k = 0; k<person_ids.size(); k++){
                                    //sqlbefhel insert into autoren_buecher produkt_nr, person_id
                                    insertBuchPerson(conn, produkt_nr, person_ids.get(k));
                                    writer.write("\n" +"Save insertBuchPerson:" + produkt_nr);
                                    writer.write("\n" +"Save insertBuchPerson:" + person_ids.get(k));
                                                                    }
                                break;
                            case "Music":   
                                for(String x : label){
                                    //sqlbefehl for insert into musikcd
                                    insertMusikCd(conn, produkt_nr, x, erscheinungsdatum);
                                    writer.write("\n" +"Save insertMusikCd: " + produkt_nr);
                                    writer.write("\n" +"Save insertMusikCd: " + x);
                                   writer.write("\n" +"Save insertMusikCd: " + erscheinungsdatum);
                                    
                                }
                                for(int k = 0; k<person_ids.size(); k++){
                                    //sqlbefehl insert into kuenstler_cds produkt_nr, person_id
                                    insertMusikPerson(conn, produkt_nr, person_ids.get(k));
                                    writer.write("\n" +"Save musikPerson:" + produkt_nr);
                                    writer.write("\n" +"Save musikPerson:" + person_ids.get(k));
                                    
                                }
                                break;
                        }
                        //insertloop songs
                        for (String one_song : song_list){
                            //sqlbefehl insert songtitles
                            //insertSongTitle(conn, songTitle, produkt_nr2);
                            insertSongTitle(conn, one_song, produkt_nr);
                            writer.write("\n" +"Save insertSongTitle: " + one_song);
                            writer.write("\n" +"Save insertSongTitle: " + produkt_nr);
                            
                        }
                        // insertloop similars
                        for(String sim : sim_list){
                            //sql befehl insert into aehnliche_produkte produkt_nr1, produkt_nr2
                            insertSimilar(conn, produkt_nr1, sim);
                            writer.write("\n" +"Save similars: " + produkt_nr1);
                            writer.write("\n" +"Save similars: " + sim);
                            
                        }
                        personenName = new ArrayList<>();
                        person_ids = new ArrayList<>();
                        sim_list = new ArrayList<>();
                        song_list = new ArrayList<>();
                        rolle = new ArrayList<>();
                        label = new ArrayList<>();
                        verlag = new ArrayList<>();
                        
                    }
                
                }
            }
        }
    }

    public static int insertPerson(
        Connection conn, 
        String name
        )throws SQLException {
    //sql befehl insert into personen person_id, name
        PreparedStatement statement;
        String sql = "INSERT INTO personen (name) VALUES (?)";
        try{
            statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet personSet = statement.getGeneratedKeys();
                
            if (personSet.next()) {
                int personen_key = personSet.getInt(1);
                return personen_key;
            } else {
                throw new SQLException("No address ID generated!");
            } 
        } catch(SQLException e){
            if (e.getSQLState().equals("23505")) {  // PostgreSQL unique_violation SQL state
            // Now query the existing person ID
            sql = "SELECT person_id FROM personen WHERE name = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int existingPersonId = resultSet.getInt("person_id");
                return existingPersonId;
            } else {
                throw new SQLException("Person exists but ID not found.");
            }
        } else {
            // Re-throw other SQL exceptions
            throw e;
        }
        }    
    }

    public static void insertDvdPerson(Connection conn,
                                        String produkt_nr, 
                                        int person_key, String rolle) 
                                        throws SQLException, 
                                        IOException
    {

        //sql befehl insert into dvd_personen produkt_nr, person_id, rolle
        try{
            PreparedStatement statement;
            String sql = "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, produkt_nr);
            statement.setInt(2, person_key);
            statement.setString(3, rolle);
            statement.executeUpdate();
            } catch (SQLException e) {
            String sqlState = e.getSQLState();
            try (FileWriter fw = new FileWriter("errors.txt", true);
                    PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("Foreign key constraint failed for produkt_nr: " + produkt_nr);
                    pw.println("Error: " + e.getMessage());
                    pw.println("---");
                
                if (e.getSQLState().equals("23503")) { // Foreign key violation
                
                }else if ("23505".equals(sqlState)) { // Duplicate key
                        pw.println("Duplicate key error when inserting into musikcds:");
                } else {
                    throw e; // re-throw other unexpected SQL errors
                }
            }
        }
    }

    public static void insertMusikPerson(Connection conn, 
                                        String produkt_nr, 
                                        int personen_key) 
                                        throws SQLException, 
                                        IOException
    {
        //sqlbefehl insert into kuenstler_cds produkt_nr, person_id
        try{
        PreparedStatement statement;
        String sql = "INSERT INTO kuenstler_cds (produkt_nr, person_id) VALUES (?,?)";
        statement = conn.prepareStatement(sql);
        statement.setString(1, produkt_nr);
        statement.setInt(2, personen_key);
        statement.executeUpdate(); 
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            try (FileWriter fw = new FileWriter("errors.txt", true);
                    PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("Foreign key constraint failed for produkt_nr: " + produkt_nr);
                    pw.println("Error: " + e.getMessage());
                    pw.println("---");
                
                if (e.getSQLState().equals("23503")) { // Foreign key violation
                
                }else if ("23505".equals(sqlState)) { // Duplicate key
                        pw.println("Duplicate key error when inserting into musikcds:");
                } else {
                    throw e; // re-throw other unexpected SQL errors
                }
            }
        }  
    }

    public static void insertBuchPerson(
        Connection conn, 
        String produkt_nr,
        int personen_key
        ) 
        throws SQLException,
        IOException
    {
        //sqlbefhel insert into autoren_buecher produkt_nr, person_id
        try{
        PreparedStatement statement;
        String sql = "INSERT INTO autoren_buecher (produkt_nr, person_id) VALUES (?,?)";
        statement = conn.prepareStatement(sql);
        statement.setString(1, produkt_nr);
        statement.setInt(2, personen_key);
        statement.executeUpdate(); 
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            try (FileWriter fw = new FileWriter("errors.txt", true);
                    PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("Foreign key constraint failed for produkt_nr: " + produkt_nr);
                    pw.println("Error: " + e.getMessage());
                    pw.println("---");
                
                if (e.getSQLState().equals("23503")) { // Foreign key violation
                
                }else if ("23505".equals(sqlState)) { // Duplicate key
                        pw.println("Duplicate key error when inserting into musikcds:");
                } else {
                    throw e; // re-throw other unexpected SQL errors
                }
            }
        }   
    }

    public static void insertMusikCd(
        Connection conn, 
        String produkt_nr, 
        String label,
        LocalDate erscheinungsdatum) 
        throws SQLException, 
        IOException
    {
        try {
            //sqlbefhel insert into musikcds produkt_nr, label, erscheinungsdatum
            PreparedStatement statement;
            String sql = "INSERT INTO musikcds (produkt_nr, label, erscheinungsdatum) VALUES (?, ?, ?)";
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
            String sqlState = e.getSQLState();
            
            try (FileWriter fw = new FileWriter("errors.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {
                
                if ("23505".equals(sqlState)) { // Duplicate key
                    pw.println("Duplicate key error when inserting into musikcds:");
                } else if ("23503".equals(sqlState)) { // Foreign key violation
                    pw.println("Foreign key violation: related product does not exist in produkte table:");
                } else {
                    throw e; // rethrow other unexpected SQL errors
                }

                pw.println("produkt_nr: " + produkt_nr);
                pw.println("Error: " + e.getMessage());
                pw.println("-----");

            } catch (IOException ioEx) {
                System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
            }
        }
    }

    public static void insertFilmDvd(
        Connection conn,
        String produkt_nr,
        String format,
        LocalTime laufzeit,
        int region_code
    )throws SQLException, IOException
    {
        //insert into dvds format, laufzeit, regioncode
        try {
            PreparedStatement statement;
            String sql = "INSERT INTO dvds (produkt_nr, format, laufzeit, region_code) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, produkt_nr);
            statement.setString(2, format);
            statement.setTime(3, Time.valueOf(laufzeit));
            statement.setInt(4, region_code);
            statement.executeUpdate();
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            
            try (FileWriter fw = new FileWriter("errors.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {
                
                if ("23505".equals(sqlState)) { // Duplicate key
                    pw.println("Duplicate key error when inserting into musikcds:");
                } else if ("23503".equals(sqlState)) { // Foreign key violation
                    pw.println("Foreign key violation: related product does not exist in produkte table:");
                } else {
                    throw e; // rethrow other unexpected SQL errors
                }

                pw.println("produkt_nr: " + produkt_nr);
                pw.println("Error: " + e.getMessage());
                pw.println("-----");

            } catch (IOException ioEx) {
                System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
            }
        }
    } 

    public static void insertLiteraturBuch(
        Connection conn,
        String produkt_nr,
        int seitenzahl,
        LocalDate erscheinungsdatum,
        String isbn,
        String verlag
    ) throws SQLException, IOException{
        //sql befehl insert into buecher seitenzahl, erscheinungsdatum, isbn, verlag
        try {
            PreparedStatement statement;
            String sql = "INSERT INTO buecher (produkt_nr, seitenzahl, erscheinungsdatum, isbn, verlag) VALUES (?, ?, ?, ?, ?)";
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
    }

    public static void insertSimilar(
                                        Connection conn,
                                        String produkt_nr1,
                                        String produkt_nr2
    )throws SQLException{
        try {
            //sqlbefhel insert into aehnliche_produkte produkt_nr1, produkt_nr2
            PreparedStatement statement;
            String sql = "INSERT INTO aehnliche_produkte (produkt_nr1, produkt_nr2) VALUES (?, ?)";
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
                    pw.println("produkt_nr1: " + produkt_nr1 + ", produkt_nr2: " + produkt_nr2);
                } else if ("23505".equals(sqlState)) { // Duplicate key
                    pw.println("Duplicate Key Violation:");
                    pw.println("produkt_nr1: " + produkt_nr1 + ", produkt_nr2: " + produkt_nr2);
                } else if ("23514".equals(sqlState)) { // Check constraint violation
                     pw.println("Check Constraint Violation:");
                } else {
                    // Unknown SQL error, rethrow
                    throw e;
                }

                pw.println("produkt_nr1: " + produkt_nr1 + ", produkt_nr2: " + produkt_nr2);
                pw.println("Error Message: " + e.getMessage());
                pw.println("---");  
            } catch (IOException ioEx) {
                System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
            }
        }
    }

    public static void insertSongTitle(
        Connection conn,
        String songTitle,
        String produkt_nr
    )throws SQLException
    {
        try {
            // SQL insert for title
            PreparedStatement statement;
            String sql = "INSERT INTO titel (name, produkt_nr) VALUES (?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, songTitle);
            statement.setString(2, produkt_nr);
            statement.executeUpdate();

        } catch (SQLException e) {
            // Log duplicate key or other errors to a file
            try (FileWriter fw = new FileWriter("errors.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {
                pw.println("Failed to insert track: \"" + songTitle + "\" for product " + produkt_nr);
                pw.println("Error: " + e.getMessage());
                pw.println("---");
            } catch (IOException ioEx) {
                System.err.println("Failed to write to error log: " + ioEx.getMessage());
            }
        }
    }
    
    public static void insertItem(
        Connection conn, 
        String produkt_nr,
        String titel,
        Float rating,
        int verkaufsrang,
        String bild,
        String produkttyp
        ) throws SQLException, IOException
        {
        try {
                PreparedStatement statement;
                String sql = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sql);
                statement.setString(1, produkt_nr);
                statement.setString(2, titel);
                statement.setFloat(3, rating);
                statement.setInt(4, verkaufsrang);
                statement.setString(5, bild);
                statement.setString(6, produkttyp);
                statement.executeUpdate(); 
                } catch (SQLException e) {
                    try (FileWriter fw = new FileWriter("errors.txt", true);
                        PrintWriter pw = new PrintWriter(fw)) {
                        
                        pw.println("SQL error when inserting into produkte:");
                        pw.println("produkt_nr: " + produkt_nr);
                        pw.println("titel: " + titel);
                        pw.println("produkttyp: " + produkttyp);
                        pw.println("SQLState: " + e.getSQLState());
                        pw.println("Error Code: " + e.getErrorCode());
                        pw.println("Message: " + e.getMessage());
                        pw.println("-----");
                    } catch (IOException ioEx) {
                        System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                    }
                }
    }

    public static int insertShop(
    Connection conn,
    String shopName,
    int adressId
    )throws SQLException, IOException{
        String sql = "INSERT INTO filialen (name, adress_id) VALUES (?, ?)";
        
        try (
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, shopName);
            statement.setInt(2, adressId);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No shop ID generated!");
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) { // Unique constraint violation
                try (FileWriter fw = new FileWriter("errors.txt", true);
                    PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("Duplicate key error when inserting into filialen:");
                    pw.println("shop: " + shopName);
                    pw.println("Error: " + e.getMessage());
                    pw.println("-----");
                } catch (IOException ioEx) {
                    System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                }
                return -1; // Or handle as appropriate
            } else {
                throw e; // Re-throw unexpected SQL errors
            }
        }
    }

        
    

    public static int insertAdress(
        Connection conn,
        String str,
        String hausnummer,
        String zusatz,
        String zip,
        String shopName
    )throws SQLException{
        //sql befehl insert into adressen straße, hausnummer, zusatz, plz, stadt
        PreparedStatement statement;
        String sql = "INSERT INTO adressen (straße, hausnummer, zusatz, plz, stadt) VALUES (?, ?, ?, ?, ?)";
        statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, str);
        statement.setString(2, hausnummer);
        statement.setString(3, zusatz);
        statement.setInt(4, Integer.parseInt(zip));
        statement.setString(5, shopName);
        statement.executeUpdate();
        ResultSet adressSet = statement.getGeneratedKeys();
        int adressId = -1; 
        if (adressSet.next()) {
            adressId = adressSet.getInt(1);
            return adressId;
        } else {
            throw new SQLException("No address ID generated!");
        } 
    
    }

    public static void insertAngebot(
        Connection conn, 
        String produkt_nr,
        int filiale_id,
        Float preis, 
        String zustand
        )throws SQLException{
             //sql befehl insert into adressen straße, hausnummer, zusatz, plz, stadt
            try {
                PreparedStatement statement;
                String sql = "INSERT INTO angebote (produkt_nr, filiale_id, preis, zustand) VALUES (?, ?, ?::money, ?)";
                statement = conn.prepareStatement(sql);
                statement.setString(1, produkt_nr);
                statement.setInt(2, filiale_id);
                BigDecimal preisDecimal = BigDecimal.valueOf(preis);
                statement.setBigDecimal(3, preisDecimal);
                statement.setString(4, zustand);
                statement.executeUpdate();

            } catch (SQLException e) {
                // Foreign key violation
                if ("23503".equals(e.getSQLState())) {
                    try (FileWriter fw = new FileWriter("errors.txt", true);
                        PrintWriter pw = new PrintWriter(fw)) {
                        pw.println("Foreign key violation when inserting into angebote:");
                        pw.println("produkt_nr: " + produkt_nr);
                        pw.println("Error: " + e.getMessage());
                        pw.println("-----");
                    } catch (IOException ioEx) {
                        System.err.println("Could not write to errors.txt: " + ioEx.getMessage());
                    }
                } else {
                    throw e; // re-throw for all other SQL errors
                }
            }

        }
}










