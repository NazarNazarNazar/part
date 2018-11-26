To run this sample : 

1. clone repository from github: 
    $ git clone https://github.com/NazarNazarNazar/part.git
    
2. start your postgresql server;

3. create database named: part and connect to with intellijIdea using your own
   user name and password;

4. also open file /resources/db.properties and print your url database, user name and password;

5. run /resources/create.sql script to create table;

6. run /resources/populateDB.sql script to populate db with some data;

7. set your tomcat 8 with "part: war exploded" as an artifact

7. build project with maven: $ mvn clean install;

8. run application using tomcat 8

9. in browser open: http://localhost:8080