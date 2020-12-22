# repeson
A JSON-RPC Client for Java.
It uses the HttpClient of JDK11, so requests can be sent either blocking (synchronous) or not blocking (asynchronous)
## How to use it
### 1. Adding the dependency to your project
```xml
<dependency>
  <groupId>com.emiperez.repeson</groupId>
  <artifactId>repeson</artifactId>
  <version>0.5.0</version>
</dependency>
```
### 2. Configuring the Transport
1. Currently only HTTP/HTTPS Transport has been developed.
1. Create a new [HttpClient](https://openjdk.java.net/groups/net/httpclient/intro.html) and Configure it with any HTTP related properties (Authentication, Proxy, Cookie Handler, SSL and so on)
1. Build a new `HttpTransport` and inject the `HttpClient` to it
```java
Transport transport = HttpTransport.builder(httpClient).uri(uri).contentType(contentType).build();
```
### 3. Building the JsonRpcClient
```java
JsonRpcClient jsonRpcClient = JsonRpcClient.builder()
					.transport(transport)
					.version(JsonRpcVersion.v2_0)
					.idGenerator(idGenerator)
					.build();
```
### 4.- Sending the Request and getting the Response
Synchronous or blocking:
```java
JsonRpcResponse<Customer> r = jsonRpcClient.sendRequestWithDefaults("getcustomer", paramsPojo);
r.ifHasResultOrElse(customer -> System.out.println(customer.getName(),
								() -> System.out.println("No customer returned"));
```
or Asynchronous (not blocking)
```java
CompletableFuture<Customer> cc = jsonRpcClient
  .sendRequestWithDefaultsAsync("getcustomer", paramsPojo)
  .thenApply(JsonRpcResponse::getResult);
```
If the returned Type uses Generics, for example; `ArrayList<Customer>`, to prevent the [Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html), a class file, that extends JsonRpcResponse must be created,
```java
public class CustomerListResponse extends JsonRpcResponse<ArrayList<Customer>> {}
```
and passed as an argument to send methods:
```java
CustomerListResponse r = jsonRpcClient.sendRequestWithDefaults("listcustomers", paramsPojo, 
								CustomerListResponse.class);
if (r.hasResult()) {
  ArrayList<Customer> cs = r.getResult();
  //Do whatever with cs
}
```
