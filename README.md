# Pub application

Pub application, Clojure project was generated using Leiningen, Ring and Compojure library, Buddy for security, Korma for database access, Migratus for database migrations, Clojure programming language for backend, Liberator for Rest Web Services and Bootstrap framework and Javascript for frontend development.

# Running

These are steps for running the application:

Login to the MySQL server and create database pub with the following command:

CREATE DATABASE pub DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

You can change database configuration in conf/db-config.edn:

{:db "pub" :user "root" :password "root"}
Navigate to your project directory and execute the following command in the command line for database migrations:

lein migratus migrate

You can change configuration for database migration in conf/migratus-config.edn:

{:classname "com.mysql.jdbc.Driver" :subprotocol "mysql" :subname "//localhost/pub" :user "root" :password "root"}
To start a web server for the application, run:

lein ring server

Finally, you can login as admin:

username: admin
password: admin
