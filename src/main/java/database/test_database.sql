DROP TABLE IF EXISTS sources;
CREATE TABLE sources (
                         id      INTEGER PRIMARY KEY AUTOINCREMENT ,
                         source_name VARCHAR(50) NOT NULL,
                         source_url  VARCHAR(255)
);

DROP TABLE IF EXISTS recipes;
CREATE TABLE recipes (
                         id                      INTEGER PRIMARY KEY AUTOINCREMENT ,
                         recipe_name             VARCHAR(50)     NOT NULL,
                         recipe_description      VARCHAR(255),
                         source_id               INTEGER  NOT NULL,
                         portions                INTEGER  NOT NULL DEFAULT 1,

                         FOREIGN KEY (source_id) REFERENCES sources(id)

);


DROP TABLE IF EXISTS ingredients;

CREATE TABLE ingredients (
                             barcode                VARCHAR(255)    PRIMARY KEY,
                             expiry_date            DATE            NOT NULL DEFAULT (DATE('2999-1-1')),
                             ingredient_name        VARCHAR(25) NOT NULL,
                             ingredient_quantity    INTEGER  NOT NULL DEFAULT 0,
                             ingredient_quantity_type    VARCHAR(50) NOT NULL DEFAULT 'unit'
);


DROP TABLE IF EXISTS recipe_ingredient;
CREATE TABLE recipe_ingredient (
                                   ingredient_barcode         VARCHAR(255),
                                   recipe_id                  VARCHAR(255),
                                   ingredient_quantity        INTEGER      NOT NULL DEFAULT 1,

                                   PRIMARY KEY (ingredient_barcode,recipe_id),
                                   FOREIGN KEY (ingredient_barcode) REFERENCES ingredients(barcode),
                                   FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);




------------ insertions -------------
INSERT INTO sources (source_name)
VALUES
    (
        'manually added'
    );

SELECT * FROM sources;


INSERT INTO ingredients values (
                                   '1000421421',
                                   DATE('2026-01-01'),
                                   'eggs',
                                   '26',
                                   'unit'
                               );



INSERT INTO recipes
(recipe_name, recipe_description, source_id)
VALUES (
           'omelet',
           'a dish made with eggs, and only eggs',
           0
       );


INSERT INTO recipe_ingredient (ingredient_barcode, recipe_id, ingredient_quantity)
VALUES (
           '1000421421',
           1,
           2
       );

