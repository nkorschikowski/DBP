#Script for insertion of itemrelated data


import methods_parser as mp
from bs4 import BeautifulSoup
import lxml
import psycopg2
from psycopg2 import OperationalError
from datetime import date
from datetime import datetime
from datetime import timedelta

# Connect to your PostgreSQL database
try:
    conn = psycopg2.connect(
        dbname="merlin",
        user="postgres",
        password="",
        host="localhost",  # or your host
        port="5435"        # default PostgreSQL port
    )
    print("database connected")
except OperationalError as e:
    print('oh no')
    print('connection error:' + e)

#Laden der Dateien in Variablen
xml_dresden = "/Users/merlin/Downloads/media_store_project/1abgabe/dresden.xml"
xml_leipzi = "/Users/merlin/Downloads/media_store_project/1abgabe/leipzig_transformed.xml"
kategorien = "/Users/merlin/Downloads/media_store_project/1abgabe/categories.xml"
file_D = open(xml_dresden, 'r')
file_L = open(xml_leipzi, 'r')
file_kat = open(kategorien, 'r', encoding="latin1")
content_D = file_D.read()
content_L = file_L.read()
content_kat = file_kat.read()
file_L = open(xml_leipzi, 'r') 
dresdi_soup = BeautifulSoup(content_D, "xml")
leipzig_soup = BeautifulSoup(content_L, "xml") 
kategorien_soup = BeautifulSoup(content_kat, "xml")
shop_levelD = dresdi_soup.find('shop')
shop_levelL = leipzig_soup.find('shop')

#Erstellt Listen mit allen Item-Tags
items_L = [childitem for childitem in shop_levelL
           .find_all('item', recursive = False)]
items_D = [childitem for childitem in shop_levelD
           .find_all('item', recursive = False)]

#Einfügen der Produkte
# Insert Produkte: 3344 - 2325 = 1019, Verlust 3: wegen pgroup = "Buch" -> statt "Book", pgroup = "musical", no asin -> primary key (Rest duplicate key error)
for item in items_D:
    mp.dresdi_insertProdukt(conn, item)
#Insert Produkte: 3344 - 1019 = 2325 Verlust 2 wegen "no title" -> not null constraint, no asin -> primary key 
for item in items_L:
    mp.leipzi_insertProdukt(conn, item)

#Einüfügen der Produktgruppen für Dresden
# Insert Book: 715 - 309 = 406  
# Insert Music: 1940 - 1437 = 503
# Insert DVD: 689 - 579 = 110
for item in items_D:
    if item['pgroup'] == 'Book': 
       mp.dresdi_insertBook(conn, item)
    if item['pgroup'] == 'Music':
        mp.dresdi_insertCD(conn, item)
    if item['pgroup'] == 'DVD':
        mp.dresdi_insertDvd(conn, item)

#Einüfügen der Produktgruppen für Leipzig
# Insert Book: 309 --- Debug Verlag
# Insert Music: 1437 --- Labels als Liste speichern? sonst nur 1 Eintrag
# Insert DVD: 579 --- seems good
for item in items_L:
    if item['pgroup'] == 'Book': 
        mp.dresdi_insertBook(conn, item)
    if item['pgroup'] == 'Music':
        mp.leipzi_insertCD(conn, item)
    if item['pgroup'] == 'DVD':
        mp.leipzi_insertDvd(conn, item)

#insert similars
for item in items_D:
    mp.dresdi_similars(conn, item)
for item in items_L:
    mp.leipzi_similars(conn, item)

#Einfügen der Adressen
mp.adresse(conn, dresdi_soup)
mp.adresse(conn, leipzig_soup)

#Einfügen shop
mp.shop(conn, dresdi_soup)
mp.shop(conn, leipzig_soup)

#Einfügen der Angebote Dresdi, Leipzi
for item in items_D:
    mp.angebot(conn, item, "Dresden")
for item in items_D:
    mp.angebot(conn, item, 'Leipzig')
    
#Einfügen der Personen
for item in items_D:
    mp.dresdi_personen(conn, item)
for item in items_L:
    mp.leipzi_personen(conn, item)

#Einfügen der Produkt_Personen
for item in items_D:
    mp.dresdi_personen_produkt(conn, item)
for item in items_L:
    mp.leipzi_personen_produkt(conn, item)