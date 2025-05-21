-- Datei zur Erzeugung Ihres Datenbankschemas, inklusive aller dort enthaltenen Statements zur Integritätssicherung

CREATE TABLE produkte (
  produkt_nr integer PRIMARY KEY,
  titel varchar,
  rating float,
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
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr)
);

CREATE TABLE dvds (
  produkt_nr integer PRIMARY KEY,
  format varchar,
  laufzeit integer,
  region_code varchar,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr)
);

CREATE TABLE musikcds (
  produkt_nr integer PRIMARY KEY,
  label varchar,
  erscheinungsdatum date,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr)
);

CREATE TABLE titel (
  titel_id integer PRIMARY KEY,
  name varchar,
  produkt_nr integer,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr)
);

CREATE TABLE personen (
  person_id integer PRIMARY KEY,
  name varchar
);

CREATE TABLE autoren_buecher (
  produkt_nr integer,
  person_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES buecher (produkt_nr),
  FOREIGN KEY (person_id) REFERENCES personen (person_id)
);

CREATE TABLE kuenstler_cds (
  produkt_nr integer,
  person_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES musikcds (produkt_nr),
  FOREIGN KEY (person_id) REFERENCES personen (person_id)
);

CREATE TABLE dvd_personen (
  produkt_nr integer,
  person_id integer,
  rolle varchar,
  FOREIGN KEY (produkt_nr) REFERENCES dvds (produkt_nr),
  FOREIGN KEY (person_id) REFERENCES personen (person_id)
);

CREATE TABLE kategorien (
  kategorie_id integer PRIMARY KEY,
  name varchar,
  parent_kategorie_id integer,
  FOREIGN KEY (parent_kategorie_id) REFERENCES kategorien (kategorie_id)
);

CREATE TABLE produkt_kategorie (
  produkt_nr integer,
  kategorie_id integer,
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr),
  FOREIGN KEY (kategorie_id) REFERENCES kategorien (kategorie_id)
);

CREATE TABLE aehnliche_produkte (
  produkt_nr1 integer,
  produkt_nr2 integer,
  FOREIGN KEY (produkt_nr1) REFERENCES produkte (produkt_nr),
  FOREIGN KEY (produkt_nr2) REFERENCES produkte (produkt_nr)
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
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr),
  FOREIGN KEY (filiale_id) REFERENCES filialen (filiale_id)
);

CREATE TABLE kunden (
  kunden_id integer PRIMARY KEY,
  adresse varchar,
  kontonummer varchar
);

CREATE TABLE kaeufer_kauf (
  kauf_id integer PRIMARY KEY,
  kunden_id integer,
  kaufdatum timestamp,
  FOREIGN KEY (kunden_id) REFERENCES kunden (kunden_id)
);

CREATE TABLE kauf_produkt (
  kauf_id integer,
  produkt_nr integer,
  einzelpreis decimal,
  FOREIGN KEY (kauf_id) REFERENCES kaeufer_kauf (kauf_id),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr)
);

CREATE TABLE rezensionen (
  rezension_id integer PRIMARY KEY,
  kunden_id integer,
  produkt_nr integer,
  bewertung integer,
  text text,
  rezensionsdatum timestamp,
  FOREIGN KEY (kunden_id) REFERENCES kunden (kunden_id),
  FOREIGN KEY (produkt_nr) REFERENCES produkte (produkt_nr)
);