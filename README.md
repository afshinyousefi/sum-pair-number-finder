# sum-pair-number-finder
find pair numbers in list are equals to a specific number in a optimize way.
1- after clone and project yous can send a specific number and three properties about your list.
   1-2 to run project you need to install java 21 and postgres sql in your device.
   1-3 add this two line in application.properties file 
   ```xml
     spring.datasource.username=yourUsername probably postgres
    spring.datasource.password=yourPassword you set your password when install postgres
```
2- call this post api in postman: "http://localhost:8082//api/v1/pairNumber/calculate-pair-count".
3- this project have swagger dependency in it. so you can call this endpoint in any browser: "http://localhost:8082/swagger-ui/index.html#/pair-count-controller/save"
4- this is json exaple to send: 
```json
{
  "targetNumber": 900,
  "numberListSize": 1000,
  "minNumber": 1,
  "maxNumber": 1000
}
```
5- in the test section you will see a unit test for calculate method. this method is responsible for find pair numbers. you can change generated list and see output. 
6- furthermore in app section you can find what was your random list in log and in the body of your response.
