# Speciome
## Project synopsis
Speciome project aims to help researchers organize, curate and share collections
of biological specimens (plants, insects, animals etc).

## Setup instructions
### Prerequisites
The following needs to be installed before building the project:
* Java 17
* NodeJS 16
* NPM 7.15+
* PostgreSQL 14.2:
  * A database with the following configuration needs to be created:
    * username: `postgres`
    * password: `123`
    * database name: `speciome`
  * If you will specify your own database configuration, you need to modify `application-dev.properties` in `spring-data-jpa` module

### Project build & run
* Build project - run from repository root
  ```shell
  ./gradlew build
  ```
* The project offers two options to run the application:
  
  * Run frontend server (for frontend development) - from *./speciome-reactjs*:
    * Install all NPM packages (need to be done only on first install or when dependencies are changed):
      ```shell
      npm install
      ```
    * Run development server
      ```shell
      npm run start
      ```
      Frontend webpage will be available at http://localhost:3000/
  * Run as Gradle project - from repository root 
    ```shell
    ./gradlew bootRun
    ```    
    The application will be available at http://localhost:8081/
    
    Swagger page will be available at http://localhost:8081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config


## Contributors
In alphabetical order:
* Aleksandr Postnikov
* Aleksandra Davydova
* Anna Bogdanova
* Daniil Zatiaev
* Daniil Shakhmovedev
* Dmitrii Adamian
* Iaroslav Shmorgunov
* Olga Bogdanova
* Olga Trubetskaia
* Petr Piatkov
* Valeriia Rodina
