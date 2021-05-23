# CRUD App Practice
> CRUD App that reads from the database, exports to CSV, Serializes and Deserializes Database

## Technologies
* Java
* MySQL

## Setup
Create a local database in MySQL:
```
CREATE TABLE `telefonski_imenik` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Ime` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Priimek` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Naslov` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Telefon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Mobilni_telefon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Opomba` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  UNIQUE KEY `unique_kontakt` (`Ime`,`Priimek`,`Naslov`,`Email`,`Telefon`,`Mobilni_telefon`,`Opomba`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
Open IDE and enter your database credentials, run!

## Status
Project is: _finished_!
