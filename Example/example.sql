CREATE TABLE "example" (
    "id" varchar PRIMARY KEY,
    "name" varchar,
    "age" int,
    "city" varchar
)


INSERT INTO example (id, name, age, city)
VALUES ('8', 'Pepe', '14','Bremen');


DELETE FROM example WHERE name='Pepe';