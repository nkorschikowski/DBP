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
DROP TABLE IF EXISTS unterkategorie CASCADE;
DROP TABLE IF EXISTS produkt_kategorie CASCADE;
DROP TABLE IF EXISTS aehnliche_produkte CASCADE;
DROP TABLE IF EXISTS filialen CASCADE;
DROP TABLE IF EXISTS adressen CASCADE;
DROP TABLE IF EXISTS angebote CASCADE;
DROP TABLE IF EXISTS kunden CASCADE;
DROP TABLE IF EXISTS kauf CASCADE;
DROP TABLE IF EXISTS kauf_produkt CASCADE;
DROP TABLE IF EXISTS rezensionen CASCADE;


CREATE TABLE produkte (
  produkt_nr varchar(255) PRIMARY KEY, -- asin
  titel varchar(255),
  rating float, -- TODO: muss noch nach jedem update erneuert werden
  verkaufsrang integer , -- TODO: nach Ratings oder nach verkäufen? /// nicht NOT NULL oder UNIQUE, weil es villecht mehrer Produkte gibt, die noch nie verkauft wurden
  bild varchar(500),
  produkttyp varchar(255)
   CHECK (produkttyp IN ('Book','Music','DVD'))
);

CREATE TABLE buecher (
  produkt_nr varchar(255) PRIMARY KEY,
  seitenzahl integer,
  erscheinungsdatum date,
  isbn varchar(255), -- isbn kann auch mit 0  beginnen daher char -- ISBN sollten eigentlich nur 10-Stellige Nummern sein (in der Datenbank aber auch Einträge mit einem 'X' am Ende gesehen => wahrscheinlich invalid)
  verlag varchar(255),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE dvds (
  produkt_nr varchar(255) PRIMARY KEY,
  format varchar(255),
  laufzeit time,
  region_code smallint,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE musikcds (
  produkt_nr varchar(255) PRIMARY KEY,
  label varchar(255),
  erscheinungsdatum date,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE titel ( -- FEATURE: n:m tabelle machen (oder composite key?) für einfachere Abfrage "Auf welchen Alben finde ich das Lied Ocean von Peter" // kann ja auf mehreren sein ("Best of ...")
  titel_id SERIAL PRIMARY KEY, -- weil Songs gleich heißen können
  name varchar(255),
  produkt_nr varchar(255) NOT NULL,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE personen (
  person_id serial PRIMARY KEY, --personden werden mehrfach eingefügt, wenn sie mehrfach im datensatz vorkommen
  name varchar(255) UNIQUE NOT NULL -- ggf name als unique
);

CREATE TABLE autoren_buecher (
  produkt_nr varchar(255),
  person_id integer, -- personenid aus db ziehen
  PRIMARY KEY (produkt_nr, person_id),
  FOREIGN KEY (produkt_nr) REFERENCES buecher (produkt_nr) ON DELETE CASCADE, -- ohne Buch kein Autor
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
);

CREATE TABLE kuenstler_cds (
  produkt_nr varchar(255),
  person_id integer,
  PRIMARY KEY (produkt_nr, person_id),
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE, -- ohne CD kein Künstler
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
);

CREATE TABLE dvd_personen (
  produkt_nr varchar(255),
  person_id integer,
  rolle varchar(255),
  PRIMARY KEY (produkt_nr, person_id, rolle),
  FOREIGN KEY (produkt_nr) REFERENCES dvds (produkt_nr) ON DELETE CASCADE, -- ohne DVD keine Related Person
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE, -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
  CHECK (rolle IN ('Producer', 'Actor', 'Director'))
);

CREATE TABLE kategorien (
    kategorie_id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE unterkategorie (
  kategorie_id integer,
  unterkategorie_id SERIAL,
  PRIMARY KEY (kategorie_id, unterkategorie_id),
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE,
  FOREIGN KEY (unterkategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE,
  CHECK (kategorie_id <> unterkategorie_id) -- da keine Kategorie sich selbst als Unterkategorie haben soll
);

CREATE TABLE produkt_kategorie (
  produkt_nr varchar(255),
  kategorie_id integer,
  PRIMARY KEY (produkt_nr, kategorie_id),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE
);
-- TODO: Symmetrie optimieren?
CREATE TABLE aehnliche_produkte (
  produkt_nr1 varchar(255),
  produkt_nr2 varchar(255),
  PRIMARY KEY (produkt_nr1, produkt_nr2),
  FOREIGN KEY (produkt_nr1) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (produkt_nr2) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
  -- TODO: CHECK das a-b nicht eingetragen wird, wenn b-a existiert um Redundanz zu vermeiden + "um ähnliche Produkte zu einem Produkt zu finden muss das Produkt  in beiden spalten gesucht werden, die Ergebnisse addiert und Duplikate entfernt werden"
);

CREATE TABLE adressen (
  adress_id SERIAL PRIMARY KEY,
  straße varchar(255),
  hausnummer varchar(255), -- BSP 11a kann nict als integer abgezeichnet werden also hier string
  zusatz varchar(255),
  plz varchar(255), -- 0400 für Sachsen kann nur als string mit vorangestellter null repräsentiert werden
  stadt varchar(255)
);

CREATE TABLE filialen (
  filiale_id SERIAL PRIMARY KEY,
  name varchar(255) NOT NULL, -- nicht UNIQUE  wegen Franchises
  adress_id integer,
  FOREIGN KEY (adress_id) REFERENCES adressen (adress_id)
  ON DELETE SET NULL
  ON UPDATE CASCADE
);

CREATE TABLE angebote (
  produkt_nr varchar(255),
  filiale_id integer,
  preis money, -- nicht NOT NULL wegen Rabatt/Aktionen etc.
  zustand varchar(255),
  PRIMARY KEY (produkt_nr, filiale_id, zustand),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE CASCADE,
  CHECK  (zustand IN ('new', 'second-hand'))
);

CREATE TABLE kunden (
  person_id integer PRIMARY KEY, -- TODO: auf kunden_id ändern?
  adress_id integer, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt // Limitation/Bedingung, dass eine Adresse zum Kauf vorhanden sein muss, muss von Software gechecked werden
  kontonummer integer NOT NULL,
  FOREIGN KEY (person_id)  REFERENCES personen (person_id) ON DELETE CASCADE,
  FOREIGN KEY (adress_id) REFERENCES adressen (adress_id) ON DELETE SET NULL
);

CREATE TABLE kauf (
  kauf_id integer PRIMARY KEY,
  filiale_id integer, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt
  person_id integer, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt
  kaufdatum date NOT NULL, -- nicht automatisch vergeben (Systemtime)
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE SET NULL, -- der Kauf hat ja trotzdem stattgefunden
  FOREIGN KEY (person_id) REFERENCES kunden (person_id) ON DELETE SET NULL -- vielleicht will man die Kaufdaten noch haben auch wenn man den Kunden nichtmehr zuordnen kann
);

CREATE TABLE kauf_produkt (
  kauf_id integer,
  produkt_nr varchar(255),
  anzahl integer NOT NULL DEFAULT 1,
  einzelpreis money NOT NULL, -- muss gesetzt werden, da das eine Zeitaufnahme ist und sich das Angebot ja ändern kann
  PRIMARY KEY (kauf_id, produkt_nr),
  FOREIGN KEY (kauf_id) REFERENCES kauf (kauf_id) ON DELETE CASCADE, -- es soll ja der gesamte Kauf gelöscht werden
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE SET NULL
);

CREATE TABLE rezensionen (
  rezension_id serial PRIMARY KEY,
  person_id integer NOT NULL,
  produkt_nr varchar(255) NOT NULL,
  date date,
  summary varchar(255) NOT NULL,
  bewertung smallint NOT NULL,
  content text,
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE SET NULL, -- Rezension wurde ja getätigt, nur weil ein Kunde aus der Datenbank gelöscht wird, sollte dies ja keine Auswirkung auf das Ranking haben
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE --ich brauche keine Rezension zu einem Produkt, dass ich nicht anbiete
);
