# repeson
A JSON-RPC Client for Java.
It can be used with either Jackson or Yasson Json libraries.
It uses the HttpClient of JDK11, so request can be sent either blocking (synchronous) or not blocking (asynchronous)
## How to use it

### 1. Selecting the Json Mapping library
In the Maven POM file choose
Jackson:
```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>${jackson-databind.version}</version>
</dependency>
```
or Yasson:
```xml
<dependency>
  <groupId>org.eclipse</groupId>
  <artifactId>yasson</artifactId>
  <version>${yasson.version}</version>
</dependency>
```
### 2. Configure the Transport
1. Currently only HTTP Transport has been developed.
1. Create a new **[HttpClient](https://openjdk.java.net/groups/net/httpclient/intro.html)** and Configure it with any HTTP related properties (Authentication, Proxy, Cookie Handler, SSL and so on)
1. Build a new HttpTransport and inject the HttpClient to it
```java
Transport transport = HttpTransport.builder(httpClient).uri(uri).contentType(contentType).build();
```
### 3. Build the JsonRpcClient
```java
JsonRpcClient jsonRpcClient = JsonRpcClient.builder()
					.transport(transport)
					.version(JsonRpcVersion.v2_0)
					.idGenerator(idGenerator)
					.build();
```
### 4.- Send the Request and get the Response
Synchronous or blocking:
```java
JsonRpcResponse<Customer> r = jsonRpcClient.sendRequestWithDefaults("getcustomermethod", paramsPojo);
if (r.getError() == null) {
  Customer c = r.getResult();
  //Do whatever with c
}
```
or Asynchronous (not blocking)
```java
CompletableFuture<Customer> cc = jsonRpcClient
  .sendRequestWithDefaultsAsync("getcustomermethod", paramsPojo)
  .thenApply(JsonRpcResponse::getResult);
```