package repositories.restaurants

import models.Restaurant

import javax.inject._

/**
  * An in-memory implementation for restaurant storage.
  */
@Singleton
class InMemoryRestaurantRepository extends RestaurantRepository {
  override def readAll: List[Restaurant] = List(
    Restaurant("A", rating = 5, maxMeals = 40, maxVegetarian = 4),
    Restaurant("B", rating = 3, maxMeals = 100, maxVegetarian = 20, maxGlutenFree = 20)
  )
}
