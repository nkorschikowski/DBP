// kommentiertes Java Ladeprogramm bzw. SQL Statements zur XML-Transformation
// soll automatisch inkonsistente Datensätze ablehnen und in "abgelehnt.txt"
// schreibe
public class Main {
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        csvParser.parse("data\\reviews.csv");
    }
}