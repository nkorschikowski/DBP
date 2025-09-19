package com.uni;

import java.util.Scanner;



public class App 
{
    public static void main( String[] args )
    {
        boolean state = true;
        Interface meth = new Methods();
        Scanner sc = new Scanner(System.in);
        
        System.out.println( "Die Anwendung ist bereit!" );
        System.out.println("Eingabe wird erwartet ...");

        while(state){
            String input = sc.nextLine();
            switch(input) {
                case "init":
                    meth.init();
                    break;
                case "finish":
                    meth.finish();
                    break;
                case "getProduct":
                    meth.getProduct("B0000668PG"); //TODO: dynamic machen
                    break;
                case "getProducts":
                    System.out.println("Gib das Pattern an");
                    String pattern = sc.nextLine();
                    meth.getProducts(pattern);
                    break;
                case "getCategoryTree":
                    System.out.println("TODO"); // TODO
                    break;
                case "getProdutcsByCategoryPath": // TODO
                    System.out.println("TODO");
                    break;
                case "getTopProducts":
                    System.out.println("Gib das Threshhold an");
                    String kstring = sc.nextLine();
                    int k = Integer.parseInt(kstring);
                    meth.getTopProducts(k);
                    break;
                case "getSimilarCheaperProduct": // TODO
                    System.out.println("TODO");
                    break;
                case "addNewReview":
                    meth.addNewReview();
                    break;
                case "getTrolls":
                    System.out.println("Was soll die maximale Durchschnittsbewertung sein? z.B. 3.8 (inklusiv)");
                    double max = Double.parseDouble(sc.nextLine());
                    meth.getTrolls(max);
                    break;
                case "getOffers":
                    System.out.println("Wie lautet die Produktnummer?");
                    // String produkt_nr = sc.nextLine();
                    meth.getOffers("B000005GWE");
                    break;
                case "end":
                    System.out.println("Anwendung wird beendet!");
                    state = false;
                    sc.close();
                    break;
            }
        }

    }
}