package models

import org.scalatestplus.play.PlaySpec

class RestaurantSpec extends PlaySpec {
  val maxMeals = 10
  val maxVegetarian = 1
  val maxGlutenFree = 0
  val maxNutFree = 9
  val maxFishFree = 5

  val restaurant = Restaurant("ABC", 1, maxMeals, maxVegetarian, maxGlutenFree, maxNutFree, maxFishFree)

  "Restaurant#fulfill" should {
    "serve the highest num of meals first 1" in {
      val others = 12
      val vegetarian = 5
      val glutenFree = 1
      val nutFree = 0
      val fishFree = 4

      val newRequirement = restaurant.fulfill(Requirement(others, vegetarian, glutenFree, nutFree, fishFree))

      newRequirement mustEqual Requirement(2, 5, 1, 0, 4)
    }

    "serve the highest num of meals first 2" in {
      val others = 12
      val vegetarian = 15
      val glutenFree = 1
      val nutFree = 0
      val fishFree = 4

      val newRequirement = restaurant.fulfill(Requirement(others, vegetarian, glutenFree, nutFree, fishFree))

      newRequirement mustEqual Requirement(3, 14, 1, 0, 4)
    }
  }
}
