# meal-order-generator

Given that we have the following two restaurants:

```Scala
    Restaurant("A", rating = 5, maxMeals = 40, maxVegetarian = 4),
    Restaurant("B", rating = 3, maxMeals = 100, maxVegetarian = 20, maxGlutenFree = 20)
```
and then we issue a request for meals through HTTP GET like this:

```bash
/orders?total=50&vegetarian=5&gluten_free=7
```
we will see the following recommendation for orders (with the HTTP status code 200):

```json
[
  {"restaurant":"A","others":36,"vegetarian":4,"glutenFree":0,"nutFree":0,"fishFree":0},
  {"restaurant":"B","others":2,"vegetarian":1,"glutenFree":7,"nutFree":0,"fishFree":0}
]
```



If we ask for something that obviously cannot be fully fulfilled:

```bash
/orders?total=5000
```
the system will just confirm that with an empty recommendation (with the HTTP status code 200).
```json
[]
```

If we happen to issue an invalid meal request like this (more special meal requests than total):

```bash
/orders?total=5&nut_free=3&fish_free=3
```
or this:

```bash
/orders?total=-2
```

we'll get a reminder that the request is not a valid (with the HTTP status code 400):

```bash
Invalid Meal Request
```

If you're curious to see how the system will respond to a useless request by issuing this:
```bash
/orders?total=0
```
the system will kindly reply with an emtpy recommendation (again, with the HTTP status code 200):
```json
[]
```

## How a recommendation is made

First, we rank all the restaurants by their ratings and then look at them one by one from the one with the hightest rating
to the one with the lowest rating. 

Then for each restaurant, we make it prioritize all the requests for special meals (vegetarian, gluten free, nut free and fish free) over
the one for general (others) meals. Among the special meals, the restaurant is instructed to always prioritize those with a higher number of
requests. 

Any unfufilled meals will be passed down to the next restaurant. If there're still unfulfilled meals after going through
all the restaurants, we'll simply give an empty recommandation, signaling that fulfilling the original request is an 
unachievable goal. 

## How to run a local instance
1. Make sure you have [Docker](https://www.docker.com/community-edition#/download) installed.
2. Run this in your terminal:
```bash
docker run -it -p 80:9000 -v lib:/root/.ivy2/cache --name meal-order-generator --rm iluvzhouying/meal-order-generator
```
  then wait until you see the following in the terminal (it will take a while depending on your internet connection):
```
--- (Running the application, auto-reloading is enabled) ---

[info] p.c.s.AkkaHttpServer - Listening for HTTP on /0.0.0.0:9000

(Server started, use Enter to stop and go back to the console...)

```
3. Issue an HTTP GET request like this:
```bash
http://localhost/orders?total=50&vegetarian=5&gluten_free=7&nut_free=1&fish_free=23
```
All the query parameters are optional. The default for 'total' is 1 and 0 for all the others.

## How to review the code
This implementaion was built on the [Play Framework](https://www.playframework.com/) (Scala). The files of interest are the following (in the order for esay understanding of workflow): 
```bash
conf/routes
app/controllers/OrdersController.scala
app/models/Requirement.scala
app/models/Order.scala
app/services/OrderService.scala
app/models/Restaurant.scala
app/repositories/restaurants/RestaurantRepository.scala
app/repositories/restaurants/InMemoryRestaurantRepository.scala
```
Even if you're not familiar with Scala, you will, due to a fair amount of comments in the code, find it easy to have a general understanding of what the code is doing, especially 
if you're already fluent in Java.

The test code resides in the following files:
```bash
test/models/RequirementSpec.scala
test/models/RestaurantSpec.scala
test/services/OrderServiceSpec.scala
```
If you want to run those tests, type the following in your terminal:
```bash
docker exec meal-order-generator sbt test
```

One thing you'll probably notice in the entire implementaion is that all the variables are immutable and recursions take the place of loops. 
That's intentional because I want to see how far I can go with functional programming and the tangible benefits it has in
non-trivial programs (it's been goood so far. The biggest benefits are clean code and easy-to-fix bugs).





