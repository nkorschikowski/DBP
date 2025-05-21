package Example;

import java.io.BufferedReader;
import java.io.FileReader;

public class exampleCSV {
    public static void main(String[] args) {
        String filepath = "Example\\example.csv";
        // String filepath = "data\\reviews.csv";
        BufferedReader reader = null;
        String line = " ";

        try {
            reader = new BufferedReader(new FileReader(filepath));

            while ((line = reader.readLine()) != null) {
                String[] entry = line.split(",");
                for (String index : entry) {
                    System.out.print(index + "          ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
