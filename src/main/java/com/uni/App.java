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
                case "getTopProducts": // TODO
                    System.out.println("TODO");
                    break;
                case "getSimilarCheaperProduct": // TODO
                    System.out.println("TODO");
                    break;
                case "addNewReview": // TODO
                    System.out.println("TODO");
                    break;
                case "getTrolls": // TODO
                    System.out.println("TODO");
                    break;
                case "getOffers": // TODO
                    System.out.println("TODO");
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