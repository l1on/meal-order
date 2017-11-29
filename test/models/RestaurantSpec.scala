package models

import org.scalatestplus.play.PlaySpec

class RestaurantSpec extends PlaySpec {
  "Restaurant#fulfill" should {
    "serve the highest number of special meals first, and always serve the general meals last, as in example 1" in {
      val restaurant = Restaurant(name = "A", maxMeals = 40, maxVegetarian = 4)
      val newRequirement = restaurant.fulfill(Requirement(others = 38, vegetarian = 5, glutenFree = 7, nutFree = 0, fishFree = 0))

      newRequirement mustEqual Requirement(2, 1, 7, 0, 0)
    }

    "serve the highest number of special meals first, and always serve the general meals last, as in example 2" in {
      val restaurant = Restaurant(name = "A", maxMeals = 100, maxVegetarian = 20, maxGlutenFree = 34)
      val newRequirement = restaurant.fulfill(Requirement(others = 98, vegetarian = 5, glutenFree = 47, nutFree = 0, fishFree = 0))

      newRequirement mustEqual Requirement(37, 0, 13, 0, 0)
    }

    "serve up all the meals if it can fulfill all the meals" in {
      val restaurant = Restaurant(name = "A", maxMeals = 2000, maxVegetarian = 20, maxGlutenFree = 34, maxNutFree = 90, maxFishFree = 34)
      val newRequirement = restaurant.fulfill(Requirement(others = 98, vegetarian = 19, glutenFree = 5, nutFree = 1, fishFree = 20))

      newRequirement mustEqual Requirement(0, 0, 0, 0, 0)
    }

    "leave the requirement unchanged if it can fulfill any of it" in {
      val restaurant = Restaurant(name = "A", maxMeals = 0, maxVegetarian = 0, maxGlutenFree = 0, maxNutFree = 0, maxFishFree = 0)
      val originalRequirement = Requirement(others = 98, vegetarian = 19, glutenFree = 5, nutFree = 1, fishFree = 20)
      val newRequirement = restaurant.fulfill(originalRequirement)

      newRequirement mustEqual originalRequirement
    }

    "leave the requirement unchanged if the restaurant maxMeals is 0 " in {
      val restaurant = Restaurant(name = "A", maxMeals = 0, maxVegetarian = 200, maxGlutenFree = 15, maxNutFree = 9, maxFishFree = 30)
      val originalRequirement = Requirement(others = 98, vegetarian = 19, glutenFree = 5, nutFree = 1, fishFree = 20)
      val newRequirement = restaurant.fulfill(originalRequirement)

      newRequirement mustEqual originalRequirement
    }
  }
}
