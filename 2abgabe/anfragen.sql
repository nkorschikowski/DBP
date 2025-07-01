-- 1. Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus
-- sollte funktionieren
-- TESTED
-- -- SELECT
-- --   (SELECT count(*) FROM buecher) AS Bücher,
-- --   (SELECT count(*) FROM musikcds) AS Musik_CDs,
-- --   (SELECT count(*) FROM dvds) AS DVDs

-- 2. Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating.
-- Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus.
-- Wie werden gleiche durchschnittliche Ratings behandelt?
-- sollte passen TODO: evtl. bei gleicher wertung nach anzahl der rezensionen?
-- TESTED
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Book' ORDER BY rating DESC LIMIT 5)
-- -- UNION
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Music' ORDER BY rating DESC LIMIT 5)
-- -- UNION
-- -- (SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'DVD' ORDER BY rating DESC LIMIT 5)
-- -- ORDER BY rating DESC

-- 3. Für welche Produkte gibt es im Moment kein Angebot?
-- TO TEST
-- TESTED
-- -- SELECT produkt_nr, titel -- DISTINCT?
-- -- FROM produkte NATURAL JOIN angebote
-- -- WHERE preis = NULL
-- -- Ja richtig, so aber leider haben wir den parser so gestaltet, dass angebote mit 0 oder null nicht existieren dürfen :'(
-- --daher müssen wir nach allen produkten suchen für die kein Angebot existiert :)
-- --  SELECT produkt_nr, titel
-- -- FROM produkte
-- -- WHERE produkt_nr NOT IN
-- -- (SELECT produkt_nr FROM angebote)

-- kleiner Test ob es Produkte mit mehreren Angeboten gibt
-- -- SELECT angebot_id
-- -- FROM angebote
-- -- GROUP BY angebot_id HAVING  -- bei mir wird da angebot_id verlangt -- ist richtig, hatte ich shcon gefixed
-- --     COUNT(produkt_nr) > 1

-- 4. Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?
-- sollte funktionieren
-- TESTED
-- -- SELECT produkt_nr
-- -- FROM angebote
-- -- GROUP BY produkt_nr
-- --   HAVING MAX(preis) > 2 * MIN(preis);

-- 5. Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?
-- TESTED - sollte falsch sein weil es zum Beispiel auch einträge angezeigt hatte die 2 mal 1 oder 2 mal 5 als Eintrag hatten
-- -- SELECT produkt_nr FROM rezensionen WHERE bewertung = 1
-- -- UNION ALL
-- -- SELECT produkt_nr FROM rezensionen WHERE bewertung = 5
-- -- GROUP BY (produkt_nr)
-- --   HAVING COUNT(produkt_nr) > 1
-- Die Version sollte richtig sein
-- -- SELECT produkt_nr
-- -- FROM
-- -- (SELECT produkt_nr FROM rezensionen WHERE bewertung = 1 GROUP BY produkt_nr) AS eins
-- -- NATURAL JOIN
-- -- (SELECT produkt_nr FROM rezensionen WHERE bewertung = 5 GROUP BY produkt_nr) AS fuenf
-- -- GROUP BY produkt_nr

-- SELECT produkt_nr, titel
-- FROM produkte NATURAL JOIN rezensionen -- ist natural join right? -- naturally yes :D produkte ohne bewertung fallen dadurch raus
-- GROUP BY produkt_nr
--   HAVING bewertung = 1 OR bewertung = 5


-- 6. Für wieviele Produkte gibt es gar keine Rezension?
-- PASST
-- TESTED
-- -- SELECT produkt_nr, titel
-- -- FROM produkte
-- -- WHERE produkt_nr NOT IN (SELECT DISTINCT produkt_nr FROM rezensionen)

-- Beim parser werden Produkte mit Rating 0 eingelesen,
-- alle Produkte ohne Rating haben demnach weiterhin den Wert 0
-- SELECT produkt_nr, titel
-- FROM produkte
-- WHERE rating = 0;

-- 7. Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.
-- sollte passen, aber schon etwas sus, das nur guest mehr als 10 rezensionen hat
-- TESTED
-- -- SELECT name
-- -- FROM rezensionen NATURAL JOIN personen
-- -- GROUP BY name
-- --   HAVING COUNT(person_id) >= 10


-- 8. Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CDs beteiligt sind.
-- PASST
-- TESTED
-- -- SELECT DISTINCT name
-- -- FROM autoren_buecher NATURAL JOIN personen
-- -- WHERE person_id IN (SELECT person_id FROM kuenstler_cds)
-- -- OR person_id IN (SELECT person_id FROM dvd_personen) -- hier war zwei mal 'FROM kuenstler_cds' -- ja hatte ich schon gesehen und gefixed
-- -- ORDER BY name

-- 9. Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?
-- -- -- TO TEST
-- -- SELECT AVG(count)
-- -- FROM (
-- -- SELECT COUNT(titel_id) AS count
-- -- FROM titel
-- -- GROUP BY produkt_nr) -- hier werden in jeder spalte 1 angezeigt

-- -- -- TODO: wir speichern lediglich einzelne musik titel keine cds heißt man muss noch nach album namen gruppieren
-- -- -- Daher lässt sich das mit unserem db design gar nicht prüfen doder? doch können wir,
-- -- -- da musikcds die alben speichert und in titel die anzahl der titel pro cd stecken. Ok das hast du alles auch so gemacht,
-- -- -- brauche ein päuschen. Aber hab gerade gefunden unsere titel tabelle hat einen uniqie auf dem foreign key deswegen wurde immer nur
-- -- -- ein titel hunzugefügt und der parser scheint keinen error zu schmeißen: produkt_nr varchar(255) UNIQUE NOT NULL
-- -- -- wenn die unique eigenschaft entfernt wird sollte passen :) -- okay



-- -- -- 10. Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie?
-- -- -- Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie.
-- -- -- Erstellen Sie eine rekursive Anfrage, die zu jedem Produkt dessen Hauptkategorie bestimmt.
-- -- WITH RECURSIVE hauptkategorie
-- -- AS (
-- -- -- Base case
-- -- SELECT -- kategorie des produkts
-- -- UNION ALL
-- -- -- Recursive case
-- -- SELECT ...     -- alle überkatorien
-- -- FROM cte_name ... )
-- -- SELECT * FROM cte_name; -- die kategorie wo oberkategorie = null


-- -- -- 11. Welche Produkte werden in allen Filialen angeboten?
-- -- -- Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Filialen funktioniert.
-- -- -- Hinweis: Beachten Sie, dass ein Produkt mehrfach von einer Filiale angeboten werden kann (z.B. neu und gebraucht).
-- -- SELECT produkt_nr
-- -- FROM angebote
-- -- GROUP BY

-- -- -- 12. In wieviel Prozent der Fälle der Frage 11 gibt es in Leipzig das preiswerteste Angebot?
