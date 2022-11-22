# footbal-manager
Backend, Java. Football Manager  
Frontend part https://github.com/KursaKostyantyn/football_manager_react.git  
A football team/player management system Rest server. 

I mastered:
* Java 8
* Spring Boot
* Spring Security
* Hibernate/JDBC Template
* REST API

The system supports the following functionality:
1. basic CRUD operations for working with teams and players (according to the REST style)
2. adding a player to the team
3. player transfer operation from one team to another:
  ● transfer fee = number of months of player's experience * 100000 / player's age in years
  ● commission from the team (from 0% to 10% of the transfer fee) - indicated in the information about the team
  ● the full amount (transfer fee + commission) must be withdrawn from the account of the team that buys the player and transferred to the account of the team that sells

Implemented the initial filling of the database of players and teams. 
Implemented user registration, with the possibility of different roles. 
By default ADMIN (login:ADMIN password:admin) has full rights.

Added file for Postman: footbal_manger.postman_collection.json


