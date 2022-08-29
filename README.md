# Wallet
Wallet microservice demo

###  How to run

* In order to run the application jre should be installed.  Tested jre version is 1.8.0_212.
* Pre-built `Wallet-0.0.1-SNAPSHOT.jar` is checked in to the project
* Source code can be built by gradle with command:
  - `gradlew.bat clean build` (Windows OS) or
  - `./gradlew clean build` (Linux OS).
* When the building process finishes, resulting jar will be placed to folder `./buidl/libs`.
* Recommended way of starting the jar file is to open the console, navigate to the folder containing the jar file and
  run `java -jar Wallet-0.0.1-SNAPSHOT.jar`.  The application can then be stopped by pressing Ctrl-C in the console
  window.


###  API description
Base URL for API methods is `http://localhost:8080`.  Basic authentication is used with login `test` and password
`test`.

The API contains 4 methods:
* **balance** - Current balance per player
  - type: GET
  - parameters:
    - `playerName` String (mandatory) - The name of the player (should not be empty)
  - example: [http://localhost:8080/balance?playerName=John](http://localhost:8080/balance?playerName=John)

* **credit** - Credit per player
  - type: GET
  - parameters:
    - `playerName` String (mandatory) - The name of the player (should not be empty)
    - `amount` long (mandatory) - Amount to credit (should be positive)
    - `transactionId` long (mandatory) - Unique identifier of the transaction
  - example: [http://localhost:8080/credit?playerName=John&amount=10&transactionId=1](http://localhost:8080/credit?playerName=John&amount=10&transactionId=1)

* **debit** - Debit /Withdrawal per player
  - type: GET
  - parameters:
    - `playerName` String (mandatory) - The name of the player (should not be empty)
    - `amount` long (mandatory) - Amount to debit (should be positive)
    - `transactionId` long (mandatory) - Unique identifier of the transaction
  - example: [http://localhost:8080/debit?playerName=John&amount=5&transactionId=2](http://localhost:8080/debit?playerName=John&amount=5&transactionId=2)

* **history** - Transaction history per player
  - type: GET
  - parameters:
    - `playerName` String (mandatory) - The name of the player (should not be empty)
  - example: [http://localhost:8080/history?playerName=John](http://localhost:8080/history?playerName=John)


###  General remarks

* All the operations assume existence of a player, but player management is not the part of this microservice. So there is a minimalistic
  implementation: a player is identified by not empty name and created automatically when first requested with 0 balance.

* Should you need data persistence across the restart, please switch h2 database to file mode and set up preferable path
  to the db file in `spring.datasource.url` property of the `application.properties` configuration file according to the
  example that can be found in the comment one line above the property. The configuration file can be found in
  `src/main/resources/application.properties` of the source code or in `BOOT-INF/classes/application.properties` of the
  jar file which can be opened as a regular zip archive.

* In the same configuration file you can customize username and password for basic authentication if needed.

* This project uses `lombok` library (which I am free to use) to improve code readability.  But IDE can require
  additional 3rd party plugin and configuration in order to parse the code properly (which is not supposed to happen).
  I am unsure if this is Ok to use it in the project.  Please keep in mind that
  - the build and run as described in "How to run" section should work without any additional efforts
  - if it is still of any harm I am ready to remove `lombok` library from my project.
