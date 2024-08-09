# Bound Bookstore Assignment

### Summary
This is simple application that provides a few endpoints to:
- search for books by author
- get a quote for a book by ISBN
- create an order for a quote, with a delivery address

The application relies on dummy data from two stub services that are provided as part of the assignment:
- BoogleClient
- ValdiviaClient

### Running the application
This project is implemented in Kotlin (`1.9.24`) and compiled for Java 21. Please ensure the target machine has Java 21 installed, and `java` is available on the `PATH`.

Once you've cloned the repository, or unzipped the source directory, change to the `bookstore` folder and run the following command, to build sources:
```shell
./gradlew clean build
```
To start the application, run:
```shell
java -jar ./app/build/libs/bookstore-app-0.0.1-SNAPSHOT.jar
```
This should start the application listening on port `8080` of your local machine. To test the endpoint use the following `curl` commands:

#### Search for books by author
```shell
curl -v \
http://localhost:8080/bookstore/catalog?value=Gabriel%20Garcia%20Marquez&field=AUTHOR
```
This should return a response like this:
```json
{
  "books": [
    {
      "isbn": "978-0141184999",
      "title": "One Hundred Years of Solitude",
      "author": "Gabriel Garcia Marquez"
    },
    {
      "isbn": "978-0241968628",
      "title": "Chronicle of a Death Foretold",
      "author": "Gabriel Garcia Marquez"
    }
  ]
}
```
#### Create a quote for an ISBN
```shell
curl -v \
http://localhost:8080/bookstore/quote?isbn=978-0141184999
```
This should return a response like this:
```json
{
  "id": "96e6e783-0a36-422d-8b3f-70a351383736",
  "isbn": "978-0141184999",
  "price": 10.99
}
```
#### Create an order for a quote
```shell
curl -H 'Content-Type: application/json' \
-d '{ "idempotencyId": "id1", "quoteId": "96e6e783-0a36-422d-8b3f-70a351383736", "deliveryAddress": { "addressLine1": "Wendover Court", "postcode": "NW2 2PD", "city": "London", "country": "United Kingdom" } }' \
http://localhost:8080/bookstore/order
```
This should return a response like this:
```json
{
  "id": "daa4a558-02bc-4d34-83f2-09d1aa960f4f",
  "status": "CREATED",
  "quoteId": "96e6e783-0a36-422d-8b3f-70a351383736"
}
```
#### Important
This is a very basic implementation, with a lot of things that can be improved. I have tried to cover validation and error handling, but there are some use-cases that will be missing.

### TODOs
- Better test coverage. Currently, we have some tests for the services, and one big application test to verify the main requirements.
- Support for events. This is to cover the fourth requirement in the initial assignment, which deals with analytics for the marketing team.
- Improve the domain model. The current model is more or less a first pass to get the assignment done quickly.
- Search results can be cached in the catalog service.
- Use a proper database to store quotes and orders.
- Support searching by other fields such as title, ISBN, etc.
