-- 1. Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus
-- TO TEST
-- -- SELECT
-- --   (SELECT count(*) FROM buecher) AS Bücher,
-- --   (SELECT count(*) FROM musikcds) AS Musik_CDs,
-- --   (SELECT count(*) FROM dvds) AS DVDs

-- 2. Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating.
-- Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus.
-- Wie werden gleiche durchschnittliche Ratings behandelt?
-- TO TEST
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Book' ORDER BY rating DESC LIMIT 5)
-- -- UNION
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Music' ORDER BY rating DESC LIMIT 5)
-- -- UNION
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'DVD' ORDER BY rating DESC LIMIT 5)
-- -- ORDER BY rating DESC

-- 3. Für welche Produkte gibt es im Moment kein Angebot?
-- TO TEST
-- -- SELECT produkt_nr, titel -- DISTINCT?
-- -- FROM produkte NATURAL JOIN angebote
-- -- WHERE preis = NULL

-- kleiner Test ob es Produkte mit mehreren Angeboten gibt
-- -- SELECT *
-- -- FROM angebote
-- -- GROUP BY
-- --   angebote HAVING
-- --     COUNT(produkt_nr) > 1

-- 4. Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?
-- TO TEST
-- -- SELECT produkt_nr
-- -- FROM angebote
-- -- GROUP BY produkt_nr
-- --   HAVING MAX(preis) > 2 * MIN(preis);

-- 5. Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?
-- TO TEST
-- -- SELECT produkt_nr FROM rezensionen WHERE bewertung = 1
-- -- UNION ALL
-- -- SELECT produkt_nr FROM rezensionen WHERE bewertung = 5
-- -- GROUP BY (produkt_nr)
-- --   HAVING COUNT(produkt_nr) > 1

-- SELECT produkt_nr, titel
-- FROM produkte NATURAL JOIN rezensionen -- ist natural join right?
-- GROUP BY produkt_nr
--   HAVING bewertung = 1 OR bewertung = 5


-- 6. Für wieviele Produkte gibt es gar keine Rezension?
-- TO TEST
-- -- SELECT produkt_nr, titel
-- -- FROM produkte
-- -- WHERE produkt_nr NOT IN (SELECT DISTINCT produkt_nr FROM rezensionen)

-- 7. Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.
-- TO TEST
-- -- SELECT name
-- -- FROM rezensionen NATURAL JOIN personen
-- -- GROUP BY name
-- --   HAVING COUNT(person_id) > 10


-- 8. Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CDs beteiligt sind.
-- TO DO
-- -- SELECT DISTINCT name
-- -- FROM autoren_buecher NATURAL JOIN personen
-- -- WHERE person_id IN (SELECT person_id FROM kuenstler_cds) OR person_id IN (SELECT person_id FROM kuenstler_cds)

-- 9. Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?


-- 10. Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie? Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie. Erstellen Sie eine rekursive Anfrage, die zu jedem Produkt dessen Hauptkategorie bestimmt.


-- 11. Welche Produkte werden in allen Filialen angeboten? Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Filialen funktioniert. Hinweis: Beachten Sie, dass ein Produkt mehrfach von einer Filiale angeboten werden kann (z.B. neu und gebraucht).


-- 12. In wieviel Prozent der Fälle der Frage 11 gibt es in Leipzig das preiswerteste Angebot?