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
  produkt_nr integer PRIMARY KEY,
  titel varchar,
  rating float, -- TODO: muss noch nach jedem update erneuert werden
  verkaufsrang integer UNIQUE, -- TODO: nach Ratings oder nach verkäufen? /// nicht NOT NULL, weil es villecht mehrer Produkte gibt, die noch nie verkauft wurden
  bild varchar, -- varchar, weil  es ne URL ist TODO: reichen 255?
  produkttyp varchar
  -- TODO: CHECK produkttyp
);

CREATE TABLE buecher (
  produkt_nr integer PRIMARY KEY,
  seitenzahl integer,
  erscheinungsdatum date,
  isbn varchar UNIQUE NOT NULL,
  verlag varchar,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE dvds (
  produkt_nr integer PRIMARY KEY,
  format varchar,
  laufzeit integer, -- TODO: type time?
  region_code varchar, -- TODO: welcher TYPE
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE musikcds (
  produkt_nr integer PRIMARY KEY,
  label varchar,
  erscheinungsdatum date,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE titel ( -- FEATURE: n:m tabelle machen (oder composite key?) für einfachere Abfrage "Auf welchen Alben finde ich das Lied Ocean von Peter" // kann ja auf mehreren sein ("Best of ...")
  titel_id integer PRIMARY KEY, -- weil Songs gleich heißen können
  name varchar,
  produkt_nr integer UNIQUE NOT NULL,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE
);

CREATE TABLE personen (
  person_id integer PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE autoren_buecher (
  produkt_nr integer,
  person_id integer,
  PRIMARY KEY (produkt_nr, person_id),
  FOREIGN KEY (produkt_nr) REFERENCES buecher (produkt_nr) ON DELETE CASCADE, -- ohne Buch kein Autor
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
);

CREATE TABLE kuenstler_cds (
  produkt_nr integer,
  person_id integer,
  PRIMARY KEY (produkt_nr, person_id),
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr) ON DELETE CASCADE, -- ohne CD kein Künstler
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
);

CREATE TABLE dvd_personen ( -- TODO aufteilen in die Rollen?
  produkt_nr integer,
  person_id integer,
  rolle varchar,
  PRIMARY KEY (produkt_nr, person_id, rolle),
  FOREIGN KEY (produkt_nr) REFERENCES dvds (produkt_nr) ON DELETE CASCADE, -- ohne DVD keine Related Person
  FOREIGN KEY (person_id) REFERENCES personen (person_id) ON DELETE CASCADE, -- wenn die Person Weg ist, existiert das Produkt noch, sie haben nur nichts mehr miteinander zu tun
  CHECK (rolle IN ('Producer', 'Actor', 'Director'))
);

CREATE TABLE kategorien (
    kategorie_id integer PRIMARY KEY,
    name varchar UNIQUE NOT NULL
);

CREATE TABLE unterkategorie ( -- TODO: reicht nicht aus, da eine Kategorie eine oder mehrere Unterkategorien besitzen kann
  kategorie_id integer,
  unterkategorie_id integer,
  PRIMARY KEY (kategorie_id, unterkategorie_id),
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE,
  FOREIGN KEY (unterkategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE
  -- TODO: CHECK kat 1 != kat 2
);

CREATE TABLE produkt_kategorie (
  produkt_nr integer,
  kategorie_id integer,
  PRIMARY KEY (produkt_nr, kategorie_id),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id) ON DELETE CASCADE
);

CREATE TABLE aehnliche_produkte ( -- um ähnliche Produkte zu einem Produkt zu finden muss das Produkt  in beiden spalten gesucht werden, die Ergebnisse addiert und Duplikate entfernt werden
  produkt_nr1 integer,
  produkt_nr2 integer,
  PRIMARY KEY (produkt_nr1, produkt_nr2),
  FOREIGN KEY (produkt_nr1) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (produkt_nr2) REFERENCES produkte (produkt_nr) ON DELETE CASCADE
  -- TODO: CHECK das a-b nicht eingetragen wird, wenn b-a existiert
);

CREATE TABLE filialen (
  filiale_id integer PRIMARY KEY,
  name varchar NOT NULL, -- nicht UNIQUE  wegen Franchises
  anschrift varchar
);

CREATE TABLE angebote (
  produkt_nr integer,
  filiale_id integer,
  preis decimal, -- nicht NOT NULL wegen Rabatt/Aktionen etc.
  zustand varchar, -- TODO: wie viele gibts? CHECK?
  PRIMARY KEY (produkt_nr, filiale_id),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE,
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE CASCADE
);

CREATE TABLE adressen (
  adress_id int PRIMARY KEY,
  straße varchar,
  hausnummer int,
  zusatz varchar,
  plz int,
  stadt varchar
);

CREATE TABLE kunden ( -- TODO: mit Personen verknüpfen ( auch wegen Name) // gefordert sind bei Kunden aber nur Anschrift und Kontonummer
  person_id integer PRIMARY KEY,
  adress_id int, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt // Limitation/Bedingung, dass eine Adresse zum Kauf vorhanden sein muss, muss von Software gechecked werden
  kontonummer varchar NOT NULL,
  FOREIGN KEY (person_id)  REFERENCES personen (person_id) ON DELETE CASCADE,
  FOREIGN KEY (adress_id) REFERENCES adressen (adress_id) ON DELETE SET NULL
);

CREATE TABLE kauf (
  kauf_id integer PRIMARY KEY,
  filiale_id integer, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt
  person_id integer, -- nicht NOT NULL ist gewollt, siehe ON DELETEs // initial werden sie ja immer gesetzt
  kaufdatum timestamp NOT NULL, -- TODO: oder date? //// nicht automatisch vergeben
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id) ON DELETE SET NULL, -- der Kauf hat ja trotzdem stattgefunden
  FOREIGN KEY (person_id) REFERENCES kunden (person_id) ON DELETE SET NULL --  #MERLIN  vielleicht will man die Kaufdaten noch haben auch wenn man den Kunden nichtmehr zuordnen kann
);

CREATE TABLE kauf_produkt ( --anzahl hinzufügen?
  kauf_id integer,
  produkt_nr integer,
  anzahl integer NOT NULL DEFAULT 1,
  einzelpreis decimal NOT NULL, -- muss gesetzt werden, da das eine Zeitaufnahme ist und sich das Angebot ja ändern kann
  PRIMARY KEY (kauf_id, produkt_nr),
  FOREIGN KEY (kauf_id) REFERENCES kauf (kauf_id) ON DELETE CASCADE, -- es soll ja der gesamte Kauf gelöscht werden
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE SET NULL
);

CREATE TABLE rezensionen (
  rezension_id integer PRIMARY KEY,
  person_id integer NOT NULL,
  produkt_nr integer NOT NULL,
  bewertung integer NOT NULL,
  text varchar, -- TODO: oder varchar?
  FOREIGN KEY (person_id) REFERENCES kunden (person_id) ON DELETE SET NULL, -- #MERLIN Rezension wurde ja getätigt, nur weil ein Kunde aus der Datenbank gelöscht wird, sollte dies ja keine Auswirkung auf das Ranking haben
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr) ON DELETE CASCADE --ich brauche keine Rezension zu einem Produkt, dass ich nicht anbiete
);