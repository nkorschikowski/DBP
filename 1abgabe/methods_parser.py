from datetime import date
from datetime import datetime
from datetime import timedelta

### Einfügen der Produkte Leipzig in Tabelle ###

def leipzi_insertProdukt(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into produkte
        insert_query = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (%s, %s, %s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        if produkt_nr == 'unknown':
            raise ValueError("Missing 'asin' attribute in item")
        titel = item.find('title').text.strip() if (item.find('title') and item.find('title').text.strip()) else None
        rating = 0 
        verkaufsrang =  item['salesrank']
        verkaufsrang = int(verkaufsrang) if verkaufsrang.isdigit() else -1
        bild = item['picture'] if item['picture'] is not "" else None
        produkttyp = item['pgroup'] if item['pgroup'] is not "" else None
        print("Insert Nr: ", produkt_nr)
        print("Insert Titel: ", titel)
        print("Insert Rank: ", verkaufsrang)
        print("Insert Pic: ", bild)
        print("Typ: ", produkttyp)
        cur.execute(insert_query, (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp))
        conn.commit()
    
    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()


### Einfügen der Produkte Dresden in Tabelle ###
def dresdi_insertProdukt(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into produkte
        insert_query = "INSERT INTO produkte (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp) VALUES (%s, %s, %s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        if produkt_nr == 'unknown':
            raise ValueError("Missing 'asin' attribute in item")
        titel = item.find('title').text.strip() if (item.find('title') and item.find('title').text.strip()) else None
        rating = 0 
        verkaufsrang =  item['salesrank'] 
        verkaufsrang = int(verkaufsrang) if verkaufsrang.isdigit() else -1
        bild = item.find('details')['img'] if item.find('details')['img'] is not "" else None
        produkttyp = item['pgroup'] if item['pgroup'] is not "" else None
        print("Insert Nr: ", produkt_nr)
        print("Insert Titel: ", titel)
        print("Insert Rank: ", verkaufsrang)
        print("Insert Pic: ", bild)
        print("Typ: ", produkttyp)
        cur.execute(insert_query, (produkt_nr, titel, rating, verkaufsrang, bild, produkttyp))
        conn.commit()
    
    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()

### Einfügen der Bücher aus Leipzi ###
def leipzi_insertBook(conn, item):
    try:
        cur = conn.cursor()

         # Proceed with insertion into musicCD
        insert_query = "INSERT INTO buecher (produkt_nr, seitenzahl, erscheinungsdatum, isbn, verlag) VALUES (%s, %s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        if produkt_nr == 'unknown':
            raise ValueError("Missing 'asin' attribute in item")
        book = item.find('bookspec')
        verlag = item.find('publishers')
        publishers = [publisher['name'] for publisher in verlag.find_all('publisher')] or None
        pages = book.find('pages').text if book.find('pages') is not None else ""
        pages = int(pages) if pages.isdigit() else 0
        DEFAULT_DATE = date(1000, 1, 1)
        erscheinungsdatum = datetime.strptime(book.find('publication')['date'].strip(), "%Y-%m-%d").date().isoformat() if (book.find('publication')['date'] and book.find('publication')['date'].strip()) else DEFAULT_DATE.isoformat()

        isbn = book.find('isbn')['val'] if isbn is not None else None
        
        #list of publishers
        for _ in publishers if publishers else ['']:
            cur.execute(insert_query, (produkt_nr, pages, erscheinungsdatum, isbn, _ if _ is not None  else None))
            conn.commit()
            print(f"Insert Nr: {produkt_nr}\nInsert Pages: {pages}\nInsert Veröffentlicht: {erscheinungsdatum}\nInsert ISBN: {isbn}\nInsert Verlag: {_}")
            
    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()
        


### Einfügen der Bücher aus Dresdi ###   
def dresdi_insertBook(conn, item):
    try:
        cur = conn.cursor()

         # Proceed with insertion into musicCD
        insert_query = "INSERT INTO buecher (produkt_nr, seitenzahl, erscheinungsdatum, isbn, verlag) VALUES (%s, %s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')         
        if produkt_nr == 'unknown':             
            raise ValueError("Missing 'asin' attribute in item")
        book = item.find('bookspec')
        verlag = item.find('publishers').text
        publishers = [publisher.text for publisher in item.find('publishers').find_all('publisher')] or None
        pages = book.find('pages').text if book.find('pages') is not None else ""
        pages = int(pages) if pages.isdigit() else 0
        DEFAULT_DATE = date(1000, 1, 1)
        erscheinungsdatum = datetime.strptime(book.find('publication')['date'].strip(), "%Y-%m-%d").date().isoformat() if (book.find('publication')['date'] and book.find('publication')['date'].strip()) else DEFAULT_DATE.isoformat()


        isbn = book.find('isbn')['val'] if (book.find('isbn') is not None and 'val' in book.find('isbn').attrs) else None

        
        for _ in publishers if publishers else ['']:
            cur.execute(insert_query, (produkt_nr, pages, erscheinungsdatum, isbn, _ if _ is not None  else None))
            conn.commit()
            print(f"Insert Nr: {produkt_nr}\nInsert Pages: {pages}\nInsert Veröffentlicht: {erscheinungsdatum}\nInsert ISBN: {isbn}\nInsert Verlag: {_}")


    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()


### Einfügen der CDs aus Leipzi ###
def leipzi_insertCD(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into musicCD
        insert_query = "INSERT INTO musikcds (produkt_nr, label, erscheinungsdatum) VALUES (%s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')         
        if produkt_nr == 'unknown':             
            raise ValueError("Missing 'asin' attribute in item")
        cd = item.find('musicspec')
        labels = [label['name'] for label in item.find('labels').find_all('label')] or None
        DEFAULT_DATE = date(1000, 1, 1)
        erscheinungsdatum = datetime.strptime(cd.find('releasedate').text.strip(), "%Y-%m-%d").date().isoformat() if (cd.find('releasedate') and cd.find('releasedate').text.strip()) else DEFAULT_DATE.isoformat()


        for _ in labels if labels else ['']:
            cur.execute(insert_query, (produkt_nr, _ if _ is not "" else None, erscheinungsdatum))
            conn.commit()
            print(f"Insert Nr: {produkt_nr}\nInsert Label: {_}\nInsert Veröffentlicht: {erscheinungsdatum}")

        # Proceed with insertion into titel
        tracks = [track.text for track in item.find('tracks').find_all('title')] or None
        insert_query = "INSERT INTO titel (name, produkt_nr) VALUES (%s, %s);"
        for name in tracks if tracks else ['']:
            cur.execute(insert_query, (name if name is not "" else None, produkt_nr))
            conn.commit()
            print(f"Insert Track: {name} for Produkt Nr: {produkt_nr}")



    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()


### Einfügen der CDs aus Dresdi ###
def dresdi_insertCD(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into musicCD
        insert_query = "INSERT INTO musikcds (produkt_nr, label, erscheinungsdatum) VALUES (%s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')         
        if produkt_nr == 'unknown':             
            raise ValueError("Missing 'asin' attribute in item")
        cd = item.find('musicspec')
        labels = [label.text for label in item.find('labels').find_all('label')] or None
        DEFAULT_DATE = date(1000, 1, 1)
        erscheinungsdatum = datetime.strptime(cd.find('releasedate').text.strip(), "%Y-%m-%d").date().isoformat() if (cd.find('releasedate') and cd.find('releasedate').text.strip()) else DEFAULT_DATE.isoformat()



        for _ in labels if labels else ['']:
            cur.execute(insert_query, (produkt_nr, _ if _ is not "" else None, erscheinungsdatum))
            conn.commit()
            print(f"Insert Nr: {produkt_nr}\nInsert Label: {_}\nInsert Veröffentlicht: {erscheinungsdatum}")

        # Proceed with insertion into titel
        tracks = [track.text for track in item.find('tracks').find_all('title')] or None
        insert_query = "INSERT INTO titel (name, produkt_nr) VALUES (%s, %s);"
        for name in tracks if tracks else ['']:
            cur.execute(insert_query, (name if name is not "" else None, produkt_nr))
            conn.commit()
            print(f"Insert Track: {name} for Produkt Nr: {produkt_nr}")

            
    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()


### Einfügen der DVDs aus Leipzi ###      
def leipzi_insertDvd(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into dvd
        insert_query = "INSERT INTO dvds (produkt_nr, format, laufzeit, region_code) VALUES (%s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')         
        if produkt_nr == 'unknown':             
            raise ValueError("Missing 'asin' attribute in item")
        dvd = item.find('dvdspec')
        format = dvd.find('format').text.strip() if (dvd.find('format') and dvd.find('format').text.strip()) else 'unknown'
        laufzeit = str(timedelta(minutes=int(dvd.find('runningtime').text.strip()) if (dvd.find('runningtime') and dvd.find('runningtime').text.strip()) else 0))
        region_code = item.find('regioncode').text.strip() if (item.find('regioncode') and item.find('regioncode').text.strip()) else -1

        print("Insert Nr: ", produkt_nr)
        print("Insert Format: ", format)
        print("Insert Runtime: ", laufzeit)
        print("Insert Regioncode: ", region_code)

        cur.execute(insert_query, (produkt_nr, format, laufzeit, region_code))
        conn.commit()


    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()


### Einfügen der DVDs aus Dresdi ###
def dresdi_insertDvd(conn, item):
    try:
        cur = conn.cursor()

        # Proceed with insertion into dvd
        insert_query = "INSERT INTO dvds (produkt_nr, format, laufzeit, region_code) VALUES (%s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')         
        if produkt_nr == 'unknown':             
            raise ValueError("Missing 'asin' attribute in item")
        dvd = item.find('dvdspec')
        format = dvd.find('format').text.strip() if (dvd.find('format') and dvd.find('format').text.strip()) else 'unknown'
        laufzeit = str(timedelta(minutes=int(dvd.find('runningtime').text.strip()) if (dvd.find('runningtime') and dvd.find('runningtime').text.strip()) else 0))
        region_code = item.find('regioncode').text.strip() if (item.find('regioncode') and item.find('regioncode').text.strip()) else -1


        print("Insert Nr: ", produkt_nr)
        print("Insert Format: ", format)
        print("Insert Runningtime: ", laufzeit)
        print("Insert Regioncode: ", region_code)

        cur.execute(insert_query, (produkt_nr, format, laufzeit, region_code))
        conn.commit()

    except Exception as error:
        # Log the error in error.txt
        with open("error.txt", "a") as error_file:
            error_file.write(f"Insertion failed for item {produkt_nr}: {error}\n")
        print("Oh dang. Insertion @Item exception:", error)
        print("Exception TYPE:", type(error))
        conn.rollback()

### Einfügen der ähnlichen Produkte in die Tabelle similars leipzig###
def leipzi_similars(conn, item):
    try:
        cur = conn.cursor()
        insert_query = "INSERT INTO aehnliche_produkte (produkt_nr1, produkt_nr2) VALUES (%s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        for similar in item.find_all('sim_product'):
            produkt_nr2 = similar.find('asin').text
            cur.execute(insert_query, (produkt_nr, produkt_nr2))
            conn.commit()
    except Exception as error:
        with open("error.txt", "a") as error_file:
            error_file.write(f"Error inserting similars: {error}\n")
        print(f"Error inserting similars: {error}")
        conn.rollback()


##### Einfügen der ähnlichen Produkte in die Tabelle similars ###
def dresdi_similars(conn, item):
    try:
        cur = conn.cursor()
        insert_query = "INSERT INTO aehnliche_produkte (produkt_nr1, produkt_nr2) VALUES (%s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        for similar in item.find('similars').find_all('item'):
            produkt_nr2 = similar['asin']
            cur.execute(insert_query, (produkt_nr, produkt_nr2))
            conn.commit()
    except Exception as error:
        with open("error.txt", "a") as error_file:
            error_file.write(f"Error inserting similars: {error}\n")
        print(f"Error inserting similars: {error}")
        conn.rollback()

def angebot(conn, item, filiale):
    try:
        cur = conn.cursor()
        insert_query = "INSERT INTO angebote (produkt_nr,  filiale_id, preis, zustand) VALUES (%s, %s, %s, %s);"
        produkt_nr = item.get('asin', 'unknown')
        cur.execute("SELECT filiale_id FROM filialen WHERE name=%s;", (filiale,))
        filiale_id = cur.fetchone()
        mult = float(item.find("price")['mult']) if item.find("price")['mult'] and item.find("price")['mult'].strip() else 0.0
        value = float(item.find("price").text) if item.find("price").text and item.find("price").text.strip()  else 0.0
        preis = mult * value
        zustand = item.find("price")['state'] if item.find("price")['state'] and item.find("price")['state'].strip() else 'unknown' 
        cur.execute(insert_query, (produkt_nr, filiale_id, preis, zustand))
        conn.commit()

    except Exception as error:
        with open("error.txt", "a") as error_file:
            error_file.write(f"Error inserting angebot: {error}\n")
        print(f"Error inserting angebot: {error}")
        conn.rollback()

def shop(conn, shop):
    try:
        cur = conn.cursor()
        insert_query= "INSERT INTO filialen (name, adress_id) VALUES (%s, %s)"
        filiale_name = shop.find('shop').get('name')
        cur.execute("SELECT adress_id FROM adressen WHERE stadt=%s;", (filiale_name,))
        adress_id = cur.fetchone()
        cur.execute(insert_query, (filiale_name, adress_id[0],))
        conn.commit()

    except Exception as error:
        with open("error.text", "a") as error_file:
            error_file.write(f"Error inserting shop: {error}\n")
        print(f"Error inserting shop: {error}")
        conn.rollback()

def adresse(conn, shop):
    try:
        cur = conn.cursor()
        insert_query= "INSERT INTO adressen (straße, hausnummer, zusatz, plz, stadt) VALUES (%s, %s, %s, %s, %s)"
        shop = shop.find('shop')
        straße = shop['street']
        hausnummer = 1
        zusatz = "empty"
        plz = shop['zip']
        stadt = shop['name']
        cur.execute(insert_query, (straße, hausnummer, zusatz, plz, stadt))
        conn.commit()


    except Exception as error:
        with open('error.text', 'a') as error_file:
            error_file.write(f'Error inserting adress: {error}\n')
        print(f'Error inserting adress: {error}\n')
        conn.rollback()

def leipzi_personen(conn, item):
    try:
        cur = conn.cursor()
        insert_query= "INSERT INTO personen (name) VALUES (%s)";
        person_list = [tag['name'].strip()  for tag in item.find_all(name=["author", "artist", "actor", "director"]) if tag.has_attr('name')] or None
        if person_list:    
            for person in person_list:
                print(person)
                cur.execute(insert_query, (person,))
                conn.commit()

    except Exception as error:
        with open('error.text', 'a') as error_file:
            error_file.write(F"Error inserting person: {error}\n")
        print(f"Error inserting person: {error}")
        conn.rollback()

def dresdi_personen(conn, item):
    try:
        cur = conn.cursor()
        insert_query= "INSERT INTO personen (name) VALUES (%s)";
        person_list = [tag.text.strip()  for tag in item.find_all(["author", "artist", "actor", "director"])] or None
        if person_list:    
            for person in person_list:
                print(person)
                cur.execute(insert_query, (person,))
                conn.commit()

    except Exception as error:
        with open('error.text', 'a') as error_file:
            error_file.write(F"Error inserting person: {error}\n")
        print(f"Error inserting person: {error}")
        conn.rollback()

##HINWEIS: Funktioniert aktuell nur für leipzig
def leipzi_personen_produkt(conn, item):
    try:   
        cur = conn.cursor()
        produkttyp = item['pgroup'] if item['pgroup'] is not "" else None
        if produkttyp == "DVD":
            insert_query= "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (%s, %s, %s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(name=["author", "artist", "actor", "director"]) if tag.has_attr('name')] or None
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person['name'].replace("'", "") ,))
                    person_id = cur.fetchone()
                    rolle = person.name
                    rolle = rolle[:1].upper() + rolle[1:]
                    print(produkt_nr, person_id,rolle)
                    cur.execute(insert_query, (produkt_nr, person_id[0], rolle,))
                    conn.commit()
        

        elif produkttyp == "Music":
            insert_query= "INSERT INTO kuenstler_cds (produkt_nr, person_id) VALUES (%s,%s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(name=["author", "artist", "actor", "director"]) if tag.has_attr('name')] or None
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person['name'].replace("'", "") ,))
                    person_id = cur.fetchone()
                    print(produkt_nr, person_id)
                    cur.execute(insert_query, (produkt_nr, person_id[0],))
                    conn.commit()
        

        elif produkttyp == "Book":
            insert_query= "INSERT INTO autoren_buecher (produkt_nr, person_id) VALUES (%s,%s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(name=["author", "artist", "actor", "director"]) if tag.has_attr('name')] or None
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person['name'].replace("'", "") ,))
                    person_id = cur.fetchone()
                    print(produkt_nr, person_id)
                    cur.execute(insert_query, (produkt_nr, person_id[0],))
                    conn.commit()

        else: None
    
    except Exception as error:
        with open('error.text', 'a') as error_file:
            error_file.write(f"Error inserting produkt_person: {error}\n")
        print(f"Error inserting person: {error}")
        conn.rollback()

def dresdi_personen_produkt(conn, item):
    try:   
        cur = conn.cursor()
        produkttyp = item['pgroup'] if item['pgroup'] is not "" else None
        if produkttyp == "DVD":
            insert_query= "INSERT INTO dvd_personen (produkt_nr, person_id, rolle) VALUES (%s,%s,%s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(["author", "artist", "actor", "director"])] or None
            cur = conn.cursor()
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person.text.replace("'", "") ,))
                    person_id = cur.fetchone()
                    rolle = person.name
                    rolle = rolle[:1].upper() + rolle[1:]
                    print(produkt_nr, person_id,rolle)
                    cur.execute(insert_query, (produkt_nr, person_id[0], rolle,))
                    conn.commit()
        

        elif produkttyp == "Music":
            insert_query= "INSERT INTO kuenstler_cds (produkt_nr, person_id) VALUES (%s,%s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(["author", "artist", "actor", "director"])] or None
            cur = conn.cursor()
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person.text.replace("'", "") ,))
                    person_id = cur.fetchone()
                    print(produkt_nr, person_id)
                    cur.execute(insert_query, (produkt_nr, person_id[0],))
                    conn.commit()
        

        elif produkttyp == "Book":
            insert_query= "INSERT INTO autoren_buecher (produkt_nr, person_id) VALUES (%s,%s)";
            produkt_nr = item.get('asin', 'unknown')
            person_list = [tag for tag in item.find_all(["author", "artist", "actor", "director"])] or None
            cur = conn.cursor()
            if person_list:    
                for person in person_list:
                    cur.execute("SELECT person_id FROM personen WHERE name=%s;", (person.text.replace("'", "") ,))
                    person_id = cur.fetchone()
                    print(produkt_nr, person_id)
                    cur.execute(insert_query, (produkt_nr, person_id[0],))
                    conn.commit()

        else: None
    
    except Exception as error:
        with open('error.text', 'a') as error_file:
            error_file.write(f"Error inserting produkt_person: {error}\n")
        print(f"Error inserting person: {error}")
        conn.rollback()

