# Projects - Front-end and Back-end to Intercom Challenge

### Author: Cleison Ferreira de Melo @cleisommais

[![Angular](https://img.shields.io/badge/Angular-8.0.0-DD0031.svg)](https://angular.io/)
[![Java](https://img.shields.io/badge/Java-7.0-A41F16.svg)](https://www.java.com/)

### Challenge

1. We have some customer records in a text file (customers.txt, attached) one customer per line, JSON-encoded. We want to invite any customer within 100km of our Dublin office (GPS coordinates 53.3381985, -6.2592576) to some food and drinks on us.Write a program that will read the full list of customers and output the names and user ids of matching customers (within 100km), sorted by user id (ascending).You can use the first formula from this Wikipedia article to calculate distance, don’t forget, you’ll need to convert degrees to radians. Your program should be fully tested too.

![alt text](https://photos.google.com/share/AF1QipP3WF8fDPa4MHa_H8lP4fk1KLon3ZkcpZcjZahMTblwxGg51yegO_m0yrn8NdX8uQ/photo/AF1QipNE9I1nXJLfpRanH7i3kURgXsXSVq8KwvdOdsMj?key=ZUJlaklMMjE3VGVyZ3UxZ3E3akhOdHZ3aUt4QXBB)

### Set up dependencies before test
* Install JDK 1.7+ (Required)
* Install Node 10+ (Required)
* Install Maven 3.5+ (Required)
* Install Docker 19+ (Not required)

### How to test?

* First option and recomended is to use docker. Within of the main project folder intercom-challenge run `docker-compose up --build -d`. **This step will spend some minutes**. After that, you can open `localhost` on your browser and import the file to test 2nd challenge.
* Second option is manual, run `mvn package && java -jar target/intercom-challenge-services-1.0.jar` within intercom-challenge-services folder. Run `npm install && ng serve -o` within intercom-challenge-front. After that, you can open `localhost:4200` on your browser and import the file to test 2nd challenge.

### Docker commands
- *Start and update all services in Docker-compose file*: `docker-compose up --build -d`
- *Stop all services started*: `docker-compose down`

### Important to know
- Was created several unit tests, unit and integration tests. 
- Within project main folder has the file **customers.txt** to be used in your tests.


