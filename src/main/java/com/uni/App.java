package com.uni;

import java.util.Scanner;

import com.uni.entities.Kategorie;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;




public class App 
{
    public static void main( String[] args )
    {
        boolean state = true;
        Interface meth = new Methods();
        Scanner sc = new Scanner(System.in);
        //für testing
        Kategorie wurzel = new Kategorie();
        Tablefier printer = new Tablefier();
        
        System.out.println("Die Anwendung ist bereit!");
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
                    System.out.println("Gib die Produktnummer an!");
                    meth.getProduct(sc.nextLine()); // PROD
                    // meth.getProduct("B0000668PG"); //TESTING
                    break;
                case "getProducts":
                    System.out.println("Gib das Pattern an!");
                    meth.getProducts(sc.nextLine()); // PROD
                    break;
                case "getCategoryTree":
                    wurzel = meth.getCategoryTree();
                    System.out.println("wurzel erstellt...");

                    // Print the tree
                    TreeNode treeRoot = printer.convertToTreeNode(wurzel);
                    new ListingTreePrinter().print(treeRoot);
                    
                    break;
                 case "getProductsByCategoryPath": // TODO
                    System.out.println("Eingabe des Pfades wird erwartet ...");
                    String knotenPfad = sc.nextLine();

                    String pfad = "/Formats/Audio CDs/"; //Mystery & Thrillers
                    meth.getProductsByCategoryPath(wurzel, pfad);
                    break;
                case "getTopProductsRANKING":
                    System.out.println("Gib das Threshhold an");
                    meth.getTopProductsRANKING(Integer.parseInt(sc.nextLine())); // PROD
                    break;
                case "getTopProductsRATING":
                    System.out.println("Gib das Threshhold an");
                    meth.getTopProductsRATING(Integer.parseInt(sc.nextLine())); // PROD
                    break;
                case "getSimilarCheaperProduct":
                System.out.println("Gib die Produktnummer ein!");
                // meth.getSimilarCheaperProduct(sc.nextLine()); // PROD
                meth.getSimilarCheaperProduct("3937825061"); // TESTING
                    break;
                case "addNewReview":
                    meth.addNewReview();
                    break;
                case "getTrolls":
                    System.out.println("Was soll die maximale Durchschnittsbewertung sein? z.B. 3.8 (inklusiv)");
                    meth.getTrolls(Double.parseDouble(sc.nextLine())); // PROD
                    break;
                case "getOffers":
                    System.out.println("Wie lautet die Produktnummer?");
                    // meth.getOffers(sc.nextLine()); // PROD
                    meth.getOffers("B000005GWE"); // TESTING
                    break;
                case "end":
                    System.out.println("Anwendung wird beendet!");
                    state = false;
                    sc.close();
                    break;
                default:
                    System.out.println(
                    input +
                     """        
                    ist keine valide Eingabe!\n
                    init\n
                    finish\n
                    getProduct\n
                    getProducts\n
                    getCategoryTree\n
                    getProductsByCategoryPath\n
                    getTopProducts\n
                    getSimilarCheaperProduct\n
                    addNewReview\n
                    getTrolls\n
                    getOffers\n
                    """
                    );
            }
        }

    }
}
