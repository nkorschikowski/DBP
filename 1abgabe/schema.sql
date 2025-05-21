-- Datei zur Erzeugung Ihres Datenbankschemas, inklusive aller dort enthaltenen Statements zur Integritätssicherung

DROP TABLE IF EXISTS produkte CASCADE;
DROP TABLE IF EXISTS buecher CASCADE;
DROP TABLE IF EXISTS dvds CASCADE;
DROP TABLE IF EXISTS musikcds CASCADE;
DROP TABLE IF EXISTS titel CASCADE;
DROP TABLE IF EXISTS personen CASCADE;
DROP TABLE IF EXISTS autoren_buecher CASCADE;
DROP TABLE IF EXISTS kuenstler_cds CASCADE;
DROP TABLE IF EXISTS dvd_personen CASCADE;
DROP TABLE IF EXISTS kategorien CASCADE;
DROP TABLE IF EXISTS produkt_kategorie CASCADE;
DROP TABLE IF EXISTS aehnliche_produkte CASCADE;
DROP TABLE IF EXISTS filialen CASCADE;
DROP TABLE IF EXISTS angebote CASCADE;
DROP TABLE IF EXISTS kunden CASCADE;
DROP TABLE IF EXISTS kauf CASCADE;
DROP TABLE IF EXISTS kauf_produkt CASCADE;
DROP TABLE IF EXISTS rezensionen CASCADE;


CREATE TABLE produkte (
  produkt_nr integer PRIMARY KEY,
  titel varchar,
  rating float, -- muss noch nach jedem update erneuert werden
  verkaufsrang integer,
  bild varchar,
  produkttyp varchar
);

CREATE TABLE buecher (
  produkt_nr integer PRIMARY KEY,
  seitenzahl integer,
  erscheinungsdatum date,
  isbn varchar,
  verlag varchar,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE dvds (
  produkt_nr integer PRIMARY KEY,
  format varchar,
  laufzeit integer,
  region_code varchar,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE musikcds (
  produkt_nr integer PRIMARY KEY,
  label varchar,
  erscheinungsdatum date,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

-- TODO: einen (extra) n:m Table machen, weil ein Song auch in mehreren Alben vorkommen kann? ('Best of')
CREATE TABLE titel (
  titel_id integer PRIMARY KEY, -- weil Songs gleich heißen können
  name varchar,
  produkt_nr integer,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE personen (
  person_id integer PRIMARY KEY,
  name varchar
);

CREATE TABLE autoren_buecher (
  produkt_nr integer,
  person_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES buecher (produkt_nr) ON DELETE CASCADE, -- ohne Buch kein Autor
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE SET NULL -- ohne Person Autor nur unklar, aber Buch existiert tortzdem
);

CREATE TABLE kuenstler_cds (
  produkt_nr integer,
  person_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE, -- ohne CD kein Künstler
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE SET NULL -- ohne Person kein künstler, aber CD gibts ja noch
);

CREATE TABLE dvd_personen (
  produkt_nr integer,
  person_id integer,
  rolle varchar,
  FOREIGN KEY (produkt_nr) REFERENCES dvds (produkt_nr) ON DELETE CASCADE, -- ohne DVD keine Related Person
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE SET NULL -- Ohne Person existiert Film trotzdem noch
);

CREATE TABLE kategorien (
    kategorie_id integer PRIMARY KEY,
    name varchar
);

CREATE TABLE unterkategorie ( -- TODO: reicht nicht aus, da eine Kategorie eine oder mehrere Unterkategorien besitzen kann
  kategorie_id integer,
  unterkategorie_id integer,
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE,
  FOREIGN KEY (unterkategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE
);

CREATE TABLE produkt_kategorie (
  produkt_nr integer,
  kategorie_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE
);

CREATE TABLE aehnliche_produkte (
-- a-b ist zwar theoretisch das gleiche wie b-a womit Redundanzen entstehen, man müsste aber rekursiv abfragen und Daten geben auch nur "direkt" ähnlihce Produkte an
-- und ein ähnliches Produkt von einem ähnlichen Produkt von einem ähnlichen Produkt von einem .... hat mit dem ursprünglichen vielleicht nichts mehr zu tun
-- das wäre dann mit Kategorien abgedeckt
  produkt_nr1 integer,
  produkt_nr2 integer,
  FOREIGN KEY (produkt_nr1) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (produkt_nr2) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE filialen (
  filiale_id integer PRIMARY KEY,
  name varchar,
  anschrift varchar
);

CREATE TABLE angebote (
  produkt_nr integer,
  filiale_id integer,
  preis decimal,
  zustand varchar,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE CASCADE
);

CREATE TABLE kunden (
  kunden_id integer PRIMARY KEY,
  adresse varchar,
  kontonummer varchar
);

CREATE TABLE kauf (
  kauf_id integer PRIMARY KEY,
  kunden_id integer,
  kaufdatum timestamp, -- TODO: oder date?
  FOREIGN KEY (kunden_id) REFERENCES kunden (kunden_id) ON DELETE SET NULL -- vielleicht will man die Kaufdaten noch haben auch wenn man den Kunden nichtmehr zuordnen kann
);

CREATE TABLE kauf_produkt ( --anzahl hinzufügen?
  kauf_id integer,
  produkt_nr integer,
  anzahl integer DEFAULT 1,
  filiale_id integer, -- oder das in 'kauf'-Table stecken?
  einzelpreis decimal, -- muss gesetzt werden, da das eine Zeitaufnahme ist und sich das Angebot ja ändern kann
  FOREIGN KEY (kauf_id) REFERENCES kauf (kauf_id) ON DELETE CASCADE, -- es soll ja der gesamte Kauf gelöscht werden
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE SET NULL,
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE CASCADE
);

CREATE TABLE rezensionen (
  rezension_id integer PRIMARY KEY,
  kunden_id integer,
  produkt_nr integer,
  bewertung integer,
  text varchar, -- TODO: oder text?
  rezensionsdatum date, -- TODO: oder timestamp?
  FOREIGN KEY (kunden_id) REFERENCES kunden (kunden_id) ON DELETE SET NULL, --Rezension wurde ja getätigt, nur weil ein Kunde aus der Datenbank gelöscht wird, sollte dies ja keine Auswirkung auf das Ranking haben
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE --ich brauche keine Rezension zu einem Produkt, dass ich nicht anbiete
);