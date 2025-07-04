-- 1. Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus

SELECT
  (SELECT count(*) FROM buecher) AS Bücher,
  (SELECT count(*) FROM musikcds) AS Musik_CDs,
  (SELECT count(*) FROM dvds) AS DVDs

-- 2. Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating.
-- Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus.
-- Wie werden gleiche durchschnittliche Ratings behandelt?

-- TODO: nochmal nach anzahl der ratings sortieren
(SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Book' ORDER BY rating DESC LIMIT 5)
UNION
(SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'Music' ORDER BY rating DESC LIMIT 5)
UNION
(SELECT produkttyp AS Typ, produkt_nr AS ProduktNr, rating AS Rating FROM produkte WHERE produkttyp = 'DVD' ORDER BY rating DESC LIMIT 5)
ORDER BY rating DESC

-- 3. Für welche Produkte gibt es im Moment kein Angebot?

SELECT produkt_nr, titel
FROM produkte
WHERE produkt_nr NOT IN
(SELECT produkt_nr FROM angebote)

-- -- kleiner Test ob es Produkte mit mehreren Angeboten gibt
-- SELECT angebot_id
-- FROM angebote
-- GROUP BY angebot_id HAVING  -- bei mir wird da angebot_id verlangt -- ist richtig, hatte ich shcon gefixed
--     COUNT(produkt_nr) > 1

-- 4. Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?

SELECT produkt_nr
FROM angebote
GROUP BY produkt_nr
  HAVING MAX(preis) > 2 * MIN(preis);

-- 5. Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?

SELECT produkt_nr
FROM
(SELECT produkt_nr FROM rezensionen WHERE bewertung = 1 GROUP BY produkt_nr) AS eins
NATURAL JOIN
(SELECT produkt_nr FROM rezensionen WHERE bewertung = 5 GROUP BY produkt_nr) AS fuenf
GROUP BY produkt_nr


-- 6. Für wieviele Produkte gibt es gar keine Rezension?

SELECT count(*)
FROM produkte
WHERE produkt_nr NOT IN (SELECT DISTINCT produkt_nr FROM rezensionen)


-- 7. Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.

SELECT name
FROM rezensionen NATURAL JOIN personen
GROUP BY name
  HAVING COUNT(person_id) >= 10


-- 8. Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CDs beteiligt sind.

SELECT DISTINCT name
FROM autoren_buecher NATURAL JOIN personen
WHERE person_id IN (SELECT person_id FROM kuenstler_cds)
OR person_id IN (SELECT person_id FROM dvd_personen)
ORDER BY name

-- 9. Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?

SELECT AVG(count)
FROM (
SELECT COUNT(titel_id) AS count
FROM titel
GROUP BY produkt_nr)

-- TODO: gibt es produkte ohne titel?
-- SELECT * FROM (
-- SELECT count(titel_id) AS counter
-- FROM titel
-- GROUP BY produkt_nr)
-- WHERE counter = 0


-- 10. Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie?
-- Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie.
-- Erstellen Sie eine rekursive Anfrage, die zu jedem Produkt dessen Hauptkategorie bestimmt.

WITH RECURSIVE hauptkategorien AS (
SELECT
  kategorie_id,
  kategorie_id AS hauptkategorie_id
FROM kategorien
WHERE oberkategorie_id IS NULL

UNION ALL

SELECT
  k.kategorie_id,
  hk.hauptkategorie_id
FROM kategorien k
JOIN hauptkategorien hk ON k.oberkategorie_id = hk.kategorie_id
),
produkt_hauptkategorien AS (
  SELECT
    p.produkt_nr,
    hk.hauptkategorie_id
  FROM produkt_kategorie p
  JOIN hauptkategorien hk ON p.kategorie_id = hk.kategorie_id
),
aehnlich AS (
  SELECT produkt_nr1 AS p1, produkt_nr2 AS p2 FROM aehnliche_produkte
  UNION
  SELECT produkt_nr2 AS p1, produkt_nr1 AS p2 FROM aehnliche_produkte
)

SELECT DISTINCT a.p1 AS produkt
FROM aehnlich a
JOIN produkt_hauptkategorien ph1 ON a.p1 = ph1.produkt_nr
JOIN produkt_hauptkategorien ph2 ON a.p2 = ph2.produkt_nr
WHERE ph1.hauptkategorie_id IS DISTINCT FROM ph2.hauptkategorie_id
ORDER BY produkt;


-- 11. Welche Produkte werden in allen Filialen angeboten?
-- Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Filialen funktioniert.
-- Hinweis: Beachten Sie, dass ein Produkt mehrfach von einer Filiale angeboten werden kann (z.B. neu und gebraucht).

WITH numfil AS (
    SELECT count(*)
    FROM (SELECT filiale_id
          FROM angebote
          GROUP BY filiale_id)
  ),
  ohneZustand AS (
    SELECT produkt_nr, filiale_id
    FROM angebote
    GROUP BY produkt_nr, filiale_id
  ),
  frageELF AS (
    SELECT produkt_nr, count(*)
    FROM ohneZustand
    GROUP BY produkt_nr
    HAVING count(*) = (Select * from numfil)
)

SELECT produkt_nr, count(*)
FROM ohneZustand
GROUP BY produkt_nr
HAVING count(*) = (Select * from numfil)



-- 12. In wieviel Prozent der Fälle der Frage 11 gibt es in Leipzig das preiswerteste Angebot?

WITH numfil AS (
    SELECT count(*)
    FROM (SELECT filiale_id
          FROM angebote
          GROUP BY filiale_id)
  ),
  ohneZustand AS (
    SELECT produkt_nr, filiale_id
    FROM angebote
    GROUP BY produkt_nr, filiale_id
  ),
  frageELF AS (
    SELECT produkt_nr, count(*)
    FROM ohneZustand
    GROUP BY produkt_nr
    HAVING count(*) = (Select * from numfil)
  ),
  produkt_minpreis AS (
    SELECT angebote.produkt_nr, MIN(preis) AS preis
    FROM frageELF JOIN angebote ON frageELF.produkt_nr = angebote.produkt_nr
    GROUP BY angebote.produkt_nr
  ),
  minprod_leipzig AS (
    SELECT pm.produkt_nr
    FROM produkt_minpreis pm
    JOIN angebote a ON pm.produkt_nr = a.produkt_nr AND pm.preis = a.preis
    NATURAL JOIN filialen f
    WHERE f.name = 'Leipzig'
  )

  SELECT 100.0 * (SELECT count(*) FROM minprod_leipzig) / (SELECT count(*) FROM frageELF)






