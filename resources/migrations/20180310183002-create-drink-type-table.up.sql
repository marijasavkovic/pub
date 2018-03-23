CREATE TABLE IF NOT EXISTS drink_type (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(30) NOT NULL,
  PRIMARY KEY (id));

--;;

INSERT INTO drink_type (name) VALUES
  ('Bezalkoholno pice'),
  ('Alkoholno pice'),
  ('Energetski napitak'),
  ('Topli napitak');

