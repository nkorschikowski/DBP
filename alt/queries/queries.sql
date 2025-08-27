SELECT count(*)
FROM produkte;
-- 2817
-- von 3655 = 2327 Leipzig + 1328 Dresden

SELECT count(*)
FROM rezensionen;
-- 5137
-- von 6332

SELECT count(*)
FROM produkt_kategorie;



-------------------------------

-- Gibt es Musikcds die keine Titel haben?
SELECT produkt_nr
FROM musikcds m
WHERE produkt_nr NOT IN (SELECT produkt_nr FROM titel)
-- ja, 64
-- ist für die Anfrage aber nicht schlimm, weil alle Produkte in titel ja einen titel haben


SELECT produkt_nr, count(person_id)
FROM rezensionen
GROUP BY produkt_nr
ORDER BY count DESC


---------------------
-- Anzahl Menschen die mindestens 10 Rezensionen geschrieben haben
SELECT name, COUNT(*)
FROM rezensionen NATURAL JOIN personen
GROUP BY name
  HAVING COUNT(person_id) >= 10
  -- 6 


-- -- kleiner Test ob es Produkte mit mehreren Angeboten gibt
SELECT angebot_id
FROM angebote
GROUP BY angebot_id HAVING
    COUNT(produkt_nr) = 1
-- anderer Test
SELECT angebot_id, count(*) AS count
FROM angebote
GROUP BY angebot_id
ORDER BY count DESC
