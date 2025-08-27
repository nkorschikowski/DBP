// kommentiertes Java Ladeprogramm bzw. SQL Statements zur XML-Transformation
// soll automatisch inkonsistente Datensätze ablehnen und in "abgelehnt.txt"
// schreibe
public class Main {
    public static void main(String[] args) {
        System.out.println("Start XMLParser");
        XMLparser xmlp = new XMLparser();
        try {
            xmlp.parse("data/leipzig_transformed.xml");
            xmlp.parse("data/dresden.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finished parsing XML");

        System.out.println("Start CSVParser");
        CSVParser csvParser = new CSVParser();
        csvParser.parse("data/reviews.csv");
        System.out.println("finished parsing CSV");
    }
}