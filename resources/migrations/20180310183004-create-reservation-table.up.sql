CREATE TABLE IF NOT EXISTS reservation (
  id int(11) NOT NULL AUTO_INCREMENT,
  person varchar(40) NOT NULL,
  person_number int(11) NOT NULL,
  reservation_date date NOT NULL,
  PRIMARY KEY (id));

--;;

INSERT INTO reservation (person, person_number, reservation_date) VALUES
  ('Marija Stojanovic', 2, '2018-03-23'),
  ('Marko Markovic', 5, '2018-03-15'),
  ('Petar Petrovic', 3, '2018-03-18');
