package com.uni;

import java.util.Scanner;



public class App 
{
    public static void main( String[] args )
    {
        boolean state = true;
        Methods meth = new Methods();
        Scanner sc = new Scanner(System.in);
        
        System.out.println( "Die Anwendung ist bereit!" );
        System.out.println("Eingabe wird erwartet ...");

        while(true){
            String input = sc.nextLine();
            switch(input) {
                case "init":
                    meth.init();
                    break;
                case "finish":
                    meth.finish();
                    break;
                case "getProdukt":
                    meth.getProduct("B0000668PG");
                case "end":
                    System.out.println("Anwendung wird beendet!");
                    state = false;
                    sc.close();
                    break;
            }
        }

    }
}