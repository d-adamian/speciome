# Speciome
## Project synopsis
Speciome project aims to help researchers organize, curate and share collections
of biological specimens (plants, insects, animals etc).

## Setup instructions
### Prerequisites
The following needs to be installed before building the project:
* Java 11
* NodeJS 16
* NPM 7.15+

### Project build & run
* Build project - run from repository root
  ```shell
  ./gradlew build
  ```
* Run backend server - run from repository root
  ```shell
  ./gradlew bootRun
  ```
  Swagger page will be available at http://localhost:8081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
* Install all NPM packages (need to be done only on first install or when dependencies are changed). 
  Run from *./speciome-reactjs*:
  ```shell
  npm install
  ```
* Run development frontend server - from *./speciome-reactjs*:
  ```shell
  npm run start
  ```
  Frontend webpage will be available at http://localhost:3000/

## Contributors
In alphabetical order:
* Aleksanda Davydova
* Anna Bogdanova
* Dmitrii Adamian
* Olga Bogdanova
