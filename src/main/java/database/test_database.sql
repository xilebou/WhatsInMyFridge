PRAGMA encoding = 'UTF-8';
PRAGMA FOREIGN_KEYS = OFF;
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
                         instructions            BLOB       ,
                         recipe_description      VARCHAR(255),
                         source_id               INTEGER  ,
                         portions                INTEGER  NOT NULL DEFAULT 1,

                         FOREIGN KEY (source_id) REFERENCES sources(id)

);




DROP TABLE IF EXISTS recipe_ingredient;
CREATE TABLE recipe_ingredient (
                                   ingredient_barcode         VARCHAR(255),
                                   recipe_id                  VARCHAR(255),
                                   ingredient_quantity        DECIMAL      NOT NULL DEFAULT 1,

                                   PRIMARY KEY (ingredient_barcode,recipe_id),
                                   FOREIGN KEY (ingredient_barcode) REFERENCES ingredients(barcode),
                                   FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

DROP TABLE IF EXISTS generic_ingredients;
CREATE TABLE generic_ingredients (
    generic_name    VARCHAR(255)    PRIMARY KEY,
    ingredient_unit_type    VARCHAR(50) NOT NULL DEFAULT 'UNIT'
);



DROP TABLE IF EXISTS ingredients;
CREATE TABLE ingredients (
                             barcode                VARCHAR(255)    PRIMARY KEY,
                             generic_ingredient_name        VARCHAR(25) NOT NULL,
                             full_ingredient_name           VARCHAR(255),

                             description VARCHAR(1022),
                             default_quantity   DECIMAL DEFAULT 1,
                             image       BLOB,
                             brand       VARCHAR(95),
                             category    VARCHAR(128),
    FOREIGN KEY (generic_ingredient_name) REFERENCES generic_ingredients(generic_name)
);

DROP TABLE IF EXISTS pantry_ingredients;
CREATE TABLE pantry_ingredients (
    barcode         VARCHAR(255) PRIMARY KEY,
    expiry_date            DATE            DEFAULT (DATE('2999-1-1')),
    ingredient_quantity    DECIMAL  NOT NULL DEFAULT 0,

    FOREIGN KEY (barcode) REFERENCES ingredients(barcode)
);
------------ insertions -------------
PRAGMA foreign_keys = ON;

INSERT INTO sources (source_name)
VALUES
    (
        'manually added'
    );

INSERT INTO generic_ingredients (generic_name, ingredient_unit_type)
VALUES (
        'eggs',
        'UNIT'
       );

INSERT INTO ingredients (
                         barcode,
                         generic_ingredient_name,
                         full_ingredient_name,
                         description,
                         image,
                         brand,
                         category
) VALUES (
          '00100',
          'eggs',
          'brown fancy eggs',
          'very good eggs for cooking !',
          null,
          null,
          'egg product'
         );

INSERT INTO pantry_ingredients (barcode,
                                expiry_date,
                                ingredient_quantity
) VALUES (
          '00100',
          DATE('2026-01-01'),
          12
         );




INSERT INTO recipe_ingredient (ingredient_barcode, recipe_id, ingredient_quantity)
VALUES (
           '00100',
           1,
           2
       );

