#imports:
#mac: !pip3 install bs4
#mac: !pip3 install lxml
#mac: !pip3 install psycopg2
from bs4 import BeautifulSoup
import lxml
import psycopg2
import configparser

# finale methode: einlesen der unterkategorie mit ihren items
# übergeben wird immer der aufrufende tag, also die Oberkategorie
# abbruchbedingung ist keine kinder-tags 
# kategorien ohne oberkategorie haben oberkategorie_id = 0 
def insertCategories(conn, soup, okat):
    oberkategorien_list = soup.find_all('category', recursive=False)
    oberkategorie_id=0
    okat_id = 0
    for kat in oberkategorien_list:
        #print("Oberkategory: " + kat)
        if soup.name == ('categories'):
            #Sonderfall: oberste Stufe -> Kategorien ohne Oberkategorien
            kategorie = kat.contents[0].strip('\n')
            #insertbefehl sql
            okat_id = insertKategorie(conn, kategorie, okat)
            # items einfügen
            # insertItemKategorie(conn, produkt_nr, kategorie_id)
            print("Oberkategory stripped: " + kat.contents[0].strip('\n'))
            print("Kategorie: ")
            print(kategorie)
            print(okat_id)
            items = kat.find_all('item', recursive=False)
            for _ in items:
                print('item: ')
                print(_)
        else:
            # Standard-Fall: einfügen in DB kategorie, oberkategorie -> dann Rekursionsaufruf
            # Einfügen auf dem Rückweg schlecht
            #insertbefehl sql
            kategorie = kat.contents[0].strip('\n')
            oberkategorie_id = soup.contents[0].strip('\n')
            okat_id = insertKategorie(conn, kategorie, okat)
            items = kat.find_all('item', recursive=False)

            for _ in items:
                item_id= _.contents[0]
                insertItem(conn, item_id, okat_id)
            # items einfügen
            # insertItemKategorie(conn, produkt_nr, kategorie_id)
            
            #print('Kategorie: ' + kat.contents[0].strip('\n') + ' | | Oberkategorie: ' + soup.contents[0].strip('\n'))
            #print('_____-----______----_____---____---')
        #insertbefehl sql
        #insertKategorie(conn, kategorie, oberkategorie_id)
        print('Kategorie: ' + kategorie + ' | | Oberkategorie: ' + str(oberkategorie_id) + ' | | Oberkategorie: ' + str(okat))
        print('_____-----______----_____---____---')
        insertCategories(conn, kat, okat_id)


def insertKategorie(conn, kategorie, oberkategorie):
    #insert category into database
    #if not duplicate_category:
    try:
        insert_query = "INSERT INTO kategorien (name, oberkategorie_id) VALUES (%s, %s) RETURNING kategorie_id;"
        cur = conn.cursor()
        cur.execute(insert_query, (kategorie, oberkategorie,))
        serial_id = cur.fetchone()[0]
        conn.commit()
        print('Inserted: ')
        print(kategorie)
        print('Inserted Okat: ')
        print(oberkategorie)
        print('Created ID: ')
        print(serial_id)
        return serial_id
    except Exception as error:
        print ("Oh dang. Insetion exception:", error)
        print ("Exception TYPE:", type(error))
    #insert items into database

    # Einlesen der Categories.xml datei
# encoding aus dem dateiheader abgelesen
# verwendern von beautiful soup weil sie einfach schöner ist

def insertItem(conn, item_id, kategorie_id):
    #insert category into database
    #if not duplicate_category:
    try:
        insert_query = "INSERT INTO produkt_kategorie (produkt_nr, kategorie_id) VALUES (%s, %s);"
        cur = conn.cursor()
        cur.execute(insert_query, (item_id, kategorie_id,))
        conn.commit()
        print('Inserted Item: ')
        print(item_id)
        print('To Category: ')
        print(kategorie_id)
    except Exception as error:
        print ("Oh dang. Insetion @Item exception:", error)
        print ("Exception TYPE:", type(error))
    #insert items into database
kategorien = "/Users/merlin/Downloads/media_store_project/1abgabe/categories.xml"
file_kat = open(kategorien, 'r', encoding="latin1")
content_kat = file_kat.read()
kategorien_soup = BeautifulSoup(content_kat, "xml")
start_categories = kategorien_soup.find('categories')

# Connect to your PostgreSQL database
# Connect to your PostgreSQL database

#read properties
def load_properties(filename):
    props = {}
    with open(filename, 'r') as file:
        for line in file:
            line = line.strip()
            if line and not line.startswith('#'):
                key, value = line.split('=', 1)
                props[key.strip()] = value.strip().strip("'").strip('"')
    return props

try:
    props = load_properties('my.properties')

    # Extract host, port, dbname from JDBC URL
    jdbc_url = props['url']
    # Example: jdbc:postgresql://localhost:5435/merlin
    if jdbc_url.startswith('jdbc:postgresql://'):
        jdbc_url = jdbc_url[len('jdbc:postgresql://'):]
    host_port, dbname = jdbc_url.split('/')
    host, port = host_port.split(':')

    conn = psycopg2.connect(
        dbname=dbname,
        user=props['name'],
        password=props['password'],
        host=host,
        port=port
    )
    print("database connected")
    okat=None
    insertCategories(conn, start_categories, okat)
except OperationalError as e:
    print('oh no')
    print('connection error:' + e)