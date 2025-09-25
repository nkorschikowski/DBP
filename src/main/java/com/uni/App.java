package com.uni;

import java.util.Scanner;

import com.uni.entities.Kategorie;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;

import java.util.logging.Logger;
import java.util.logging.Level;




public class App 
{
    public static void main( String[] args )
    {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE); // To clean up hibernate Logging
        boolean loop = true;
        Interface meth = new Methods();
        Scanner sc = new Scanner(System.in);
        
        Kategorie wurzel = new Kategorie();
        Tablefier printer = new Tablefier();
        
        System.out.println("Die Anwendung ist bereit!");
        System.out.println("Eingabe wird erwartet ...");


        while(loop){
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
                    meth.getProduct(sc.nextLine());
                    break;
                case "getProducts":
                    System.out.println("Gib das Pattern an! Wildcards (%) sind erlaubt!");
                    meth.getProducts(sc.nextLine());
                    break;
                case "getCategoryTree":
                    wurzel = meth.getCategoryTree();
                    TreeNode treeRoot = printer.convertToTreeNode(wurzel);
                    new ListingTreePrinter().print(treeRoot);
                    break;
                 case "getProductsByCategoryPath":
                    System.out.println("Eingabe des Pfades wird erwartet ...");
                    String knotenPfad = sc.nextLine();
                    meth.getProductsByCategoryPath(wurzel, knotenPfad);
                    break;
                case "getTopProductsRANKING":
                    System.out.println("Bis zu welchem Verkaufsrang?");
                    meth.getTopProductsRANKING(readInteger(sc));
                    break;
                case "getTopProductsRATING":
                    System.out.println("Der ersten wieviel Plätze?");
                    meth.getTopProductsRATING(readInteger(sc));
                    break;
                case "getSimilarCheaperProduct":
                System.out.println("Gib die Produktnummer ein!");
                meth.getSimilarCheaperProduct(sc.nextLine());
                    break;
                case "addNewReview":
                    meth.addNewReview();
                    break;
                case "getTrolls":
                    System.out.println("Was soll die maximale Durchschnittsbewertung sein? z.B. 3.8 (inklusiv)");
                    meth.getTrolls(readDouble(sc));
                    break;
                case "getOffers":
                    System.out.println("Wie lautet die Produktnummer?");
                    meth.getOffers(sc.nextLine()); // PROD
                    break;
                case "end":
                    System.out.println("Anwendung wird beendet!");
                    loop = false;
                    sc.close();
                    break;
                default:
                    System.out.println(
                    input +
                     """        
                    ist keine valide Eingabe!
                    init
                    finish
                    getProduct
                    getProducts
                    getCategoryTree
                    getProductsByCategoryPath
                    getTopProducts
                    getSimilarCheaperProduct
                    addNewReview
                    getTrolls
                    getOffers
                    """
                    );
            }
        }

    }

    public static double readDouble(Scanner sc){
        while (true){
            try{
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Hier lief was schief. Das war warhscheinlich keine Dezimalzahl. Versuche es erneut!");
            }
        }
    }

    public static int readInteger(Scanner sc){
    while (true){
        try{
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Hier lief was schief. Das war warhscheinlich keine Ganzzahl. Versuche es erneut!");
        }
    }
    }
}
