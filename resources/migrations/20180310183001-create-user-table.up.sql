ALTER DATABASE pub CHARACTER SET utf8 COLLATE utf8_unicode_ci;

--;;

CREATE TABLE IF NOT EXISTS user (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password varchar(500) NOT NULL,
  PRIMARY KEY (id));

--;;

INSERT INTO user (username, password) VALUES ('admin', '$2a$04$RNs5Xtjo7F3upPGzSbVPTOfEOPkevYQYk96t82l4A47UenCK6l9Ou');