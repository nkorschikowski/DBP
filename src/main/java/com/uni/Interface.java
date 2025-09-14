package com.uni;

import java.util.Locale.Category;
import com.uni.entities.*;
import java.util.List;

public interface Interface {
    void init();

    void finish();

    Produkt getProduct(String produkt_nr);

    // List<Produkt> getProducts(String pattern);

    // Kategorie getCategoryTree(); //TODO: soll ein Tree werden // Parameter = Wurzelknoten?

    // List<Produkt> getProductsByCategoryPath(String categoryPath);

    // List<Produkt> getTopProducts(int k);

    // List<Produkt> getSimilarCheaperProduct(String produkt_nr);

    // void addNewReview(Rezension review);

    // List<Person> getTrolls(double minRating);

    // List<Angebot> getOffers(String produkt_nr);
}
