CREATE TABLE "produkte" (
  "produkt_nr" integer PRIMARY KEY,
  "titel" varchar,
  "rating" float,
  "verkaufsrang" integer,
  "bild" varchar,
  "produkttyp" varchar
);

CREATE TABLE "buecher" (
  "produkt_nr" integer PRIMARY KEY,
  "seitenzahl" integer,
  "erscheinungsdatum" date,
  "isbn" varchar,
  "verlag" varchar
);

CREATE TABLE "dvds" (
  "produkt_nr" integer PRIMARY KEY,
  "format" varchar,
  "laufzeit" integer,
  "region_code" varchar
);

CREATE TABLE "musikcds" (
  "produkt_nr" integer PRIMARY KEY,
  "label" varchar,
  "erscheinungsdatum" date
);

CREATE TABLE "titel" (
  "titel_id" integer PRIMARY KEY,
  "name" varchar,
  "produkt_nr" integer
);

CREATE TABLE "personen" (
  "person_id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "autoren_buecher" (
  "produkt_nr" integer,
  "person_id" integer
);

CREATE TABLE "kuenstler_cds" (
  "produkt_nr" integer,
  "person_id" integer
);

CREATE TABLE "dvd_personen" (
  "produkt_nr" integer,
  "person_id" integer,
  "rolle" varchar
);

CREATE TABLE "kategorien" (
  "kategorie_id" integer PRIMARY KEY,
  "name" varchar,
  "parent_kategorie_id" integer
);

CREATE TABLE "produkt_kategorie" (
  "produkt_nr" integer,
  "kategorie_id" integer
);

CREATE TABLE "aehnliche_produkte" (
  "produkt_nr1" integer,
  "produkt_nr2" integer
);

CREATE TABLE "filialen" (
  "filiale_id" integer PRIMARY KEY,
  "name" varchar,
  "anschrift" varchar
);

CREATE TABLE "angebote" (
  "produkt_nr" integer,
  "filiale_id" integer,
  "preis" decimal,
  "zustand" varchar
);

CREATE TABLE "kunden" (
  "kunden_id" integer PRIMARY KEY,
  "adresse" varchar,
  "kontonummer" varchar
);

CREATE TABLE "kaeufer_kauf" (
  "kauf_id" integer PRIMARY KEY,
  "kunden_id" integer,
  "kaufdatum" timestamp
);

CREATE TABLE "kauf_produkt" (
  "kauf_id" integer,
  "produkt_nr" integer,
  "einzelpreis" decimal
);

CREATE TABLE "rezensionen" (
  "rezension_id" integer PRIMARY KEY,
  "kunden_id" integer,
  "produkt_nr" integer,
  "bewertung" integer,
  "text" text,
  "rezensionsdatum" timestamp
);

COMMENT ON COLUMN "produkte"."bild" IS 'Optional';

COMMENT ON COLUMN "dvd_personen"."rolle" IS 'z.B. Actor, Creator, Director';

COMMENT ON COLUMN "rezensionen"."bewertung" IS '1 bis 5 Punkte';

ALTER TABLE "buecher" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "dvds" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "musikcds" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "titel" ADD FOREIGN KEY ("produkt_nr") REFERENCES "musikcds" ("produkt_nr");

ALTER TABLE "autoren_buecher" ADD FOREIGN KEY ("produkt_nr") REFERENCES "buecher" ("produkt_nr");

ALTER TABLE "autoren_buecher" ADD FOREIGN KEY ("person_id") REFERENCES "personen" ("person_id");

ALTER TABLE "kuenstler_cds" ADD FOREIGN KEY ("produkt_nr") REFERENCES "musikcds" ("produkt_nr");

ALTER TABLE "kuenstler_cds" ADD FOREIGN KEY ("person_id") REFERENCES "personen" ("person_id");

ALTER TABLE "dvd_personen" ADD FOREIGN KEY ("produkt_nr") REFERENCES "dvds" ("produkt_nr");

ALTER TABLE "dvd_personen" ADD FOREIGN KEY ("person_id") REFERENCES "personen" ("person_id");

ALTER TABLE "kategorien" ADD FOREIGN KEY ("parent_kategorie_id") REFERENCES "kategorien" ("kategorie_id");

ALTER TABLE "produkt_kategorie" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "produkt_kategorie" ADD FOREIGN KEY ("kategorie_id") REFERENCES "kategorien" ("kategorie_id");

ALTER TABLE "aehnliche_produkte" ADD FOREIGN KEY ("produkt_nr1") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "aehnliche_produkte" ADD FOREIGN KEY ("produkt_nr2") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "angebote" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "angebote" ADD FOREIGN KEY ("filiale_id") REFERENCES "filialen" ("filiale_id");

ALTER TABLE "kaeufer_kauf" ADD FOREIGN KEY ("kunden_id") REFERENCES "kunden" ("kunden_id");

ALTER TABLE "kauf_produkt" ADD FOREIGN KEY ("kauf_id") REFERENCES "kaeufer_kauf" ("kauf_id");

ALTER TABLE "kauf_produkt" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");

ALTER TABLE "rezensionen" ADD FOREIGN KEY ("kunden_id") REFERENCES "kunden" ("kunden_id");

ALTER TABLE "rezensionen" ADD FOREIGN KEY ("produkt_nr") REFERENCES "produkte" ("produkt_nr");
