CREATE TABLE IF NOT EXISTS drink (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(30) NOT NULL,
  price int(11) NOT NULL,
  drink_type int(11) NOT NULL,
  PRIMARY KEY (id));

--;;
ALTER TABLE drink CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
--;;

ALTER TABLE drink
  ADD CONSTRAINT drink_ibfk_1 FOREIGN KEY (drink_type) REFERENCES drink_type (id);

--;;

INSERT INTO drink (name, price, drink_type) VALUES
  ('Absinth', 270, 2),
  ('Aqua Viva', 130, 1),
  ('Caj', 115, 4),
  ('Caj sa medom', 130, 4),
  ('Caj sa rumom', 170, 4),
  ('Cappucino', 159, 4),
  ('Cockta', 195, 1),
  ('Dupli espreso', 160, 4),
  ('Espresso', 139, 4),
  ('Espresso sa mlekom', 150, 4),
  ('Fanta', 195, 1),
  ('Gin', 225, 2),
  ('Gorki list', 185, 2),
  ('Guarana', 190, 3),
  ('Ice tea', 160, 1),
  ('Jelen', 180, 2),
  ('Knjaz Miloš', 140, 1),
  ('Liker', 240, 2),
  ('Limunada', 140, 1),
  ('Martini', 280, 2),
  ('Nescafe', 160, 4),
  ('Nescafe sa šlagom', 180, 4),
  ('Nikšićko svetlo', 180, 2),
  ('Nikšićko tamno', 180, 2),
  ('Rakija', 170, 2),
  ('Red Bull', 250, 3),
  ('Schweppes', 195, 1),
  ('Staropramen', 200, 2),
  ('Tequilla', 230, 2),
  ('Toceno pivo', 185, 2),
  ('Topla cokolada', 170, 4),
  ('Ultra', 180, 3),
  ('Vinjak', 150, 2),
  ('Vodka', 230, 2),
  ('Whiskey', 260, 2);

