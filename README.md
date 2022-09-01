# DRFClient
A lightweight and user friendly client wrapper library based on Apache http client.


#### Requirements :
Java 11+
#### Preperation :
Navigate to release page , and then download the latest Jar.
After that include the jar into your classpath.<br>
If youre using gradle , then do something like this
```gradle
dependencies {
    ....
    implementation files('deps/io.github.2k16daniel-DRFClient-1.0-all.jar') 
}

```
## Documentation :

#### Retrieving Data from  server

```java
String URL = "http://Link-to-your-web/api"
// let say you have a pojo (Plain Old Java Object) named Customer
Customer mycustomer = new Customer();

DRFClient client = DRFClient.build.builder(); // construct the DRFClient first

// this method will retrieve a serialized data from server then returned into mycustomer class
mycustomer = client.retrieve(URL,null,Customer.class);
// after that you may now use your POJO for displaying data into your UI.

```

  #### Working with list of objects 

```java
String endpoint = "http://Link-to-your-web/api"
DRFClient client = DRFClient.build().builder(); // call the builder method

// let say you have a pojo (Plain Old Java Object) named Customer
 List<Customer> customer = client.retrieve(endpoint, null , new TypeReference<List<Customer>>() {});
// retrieve the data then serialize it into your mycustomer class.

// then you may now use your POJO for displaying data into your UI.

```

#### Sending Data to server
```java
String URL = "http://Link-to-your-web/api"
// let say you have a pojo (Plain Old Java Object) named Customer
Customer mycustomer = new Customer();

DRFClient client = DDRFClient.build().builder(); // construct the DRFClient first

// Pass your pojo directly to send data to server.
client.send(URL,mycustomer.);
```

#### Updating Data to server
```java
String URL = "http://Link-to-your-web/api"
// let say you have a pojo (Plain Old Java Object) named Customer
Customer mycustomer = ...

DRFClient client = DRFClient.build().builder(); // construct the DRFClient first

// Pass your pojo directly to update data to server.
client.update(URL,mycustomer);
```

#### Deleting Data from server
```java
String URL = "http://Link-to-your-web/api"

DRFClient client = DRFClient.build().builder(); // construct the DRFClient first

// Pass your URL to delete() method to delete data from server
client.delete(URL);
```
