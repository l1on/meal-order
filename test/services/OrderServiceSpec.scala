package services

import models.{Order, Requirement, Restaurant}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import repositories.restaurants.RestaurantRepository

class OrderServiceSpec extends PlaySpec with MockitoSugar {
  val restaurantRepo = mock[RestaurantRepository]
  val orderSvc = new OrderService(restaurantRepo)

  "OrderService#createOrders" should {
    "build a list of orders meeting the example set in the requirement document" in {
      val restaurants = List(
        Restaurant("A", rating = 5, maxMeals = 40, maxVegetarian = 4),
        Restaurant("B", rating = 3, maxMeals = 100, maxVegetarian = 20, maxGlutenFree = 20)
      )
      when(restaurantRepo.readAll) thenReturn restaurants

      val requirement = Requirement(others = 38, vegetarian = 5, glutenFree = 7, nutFree = 0, fishFree = 0)

      val actualOrders = orderSvc.buildOrders(requirement)

      actualOrders mustEqual List(Order("A", 36, 4, 0, 0, 0), Order("B", 2, 1, 7, 0, 0))
    }

    "return an empty list if the request cannot be fully fulfilled by all the restaurants" in {
      val restaurants = List(
        Restaurant("A", maxMeals = 4, maxVegetarian = 4),
        Restaurant("B", maxMeals = 7, maxVegetarian = 2, maxGlutenFree = 2),
        Restaurant("C", maxMeals = 5),
        Restaurant("D", maxMeals = 5, maxVegetarian = 5, maxGlutenFree = 5, maxNutFree = 5, maxFishFree = 5)
      )
      when(restaurantRepo.readAll) thenReturn restaurants

      val requirement = Requirement(others = 400, vegetarian = 100, glutenFree = 250, nutFree = 0, fishFree = 0)

      val actualOrders = orderSvc.buildOrders(requirement)

      actualOrders mustEqual List()
    }

    "return an empty list if there're no restaurants at all" in {
      when(restaurantRepo.readAll) thenReturn List()

      val requirement = Requirement(others = 400, vegetarian = 100, glutenFree = 250, nutFree = 0, fishFree = 0)

      val actualOrders = orderSvc.buildOrders(requirement)

      actualOrders mustEqual List()
    }

    "return an empty list if the requirement is empty" in {
      when(restaurantRepo.readAll) thenReturn List(
        Restaurant("A", maxMeals = 4, maxVegetarian = 4),
        Restaurant("B", maxMeals = 7, maxVegetarian = 2, maxGlutenFree = 2)
      )

      val requirement = Requirement(others = 0, vegetarian = 0, glutenFree = 0, nutFree = 0, fishFree = 0)

      val actualOrders = orderSvc.buildOrders(requirement)

      actualOrders mustEqual List()
    }

    "remove a restaurant from the list if the restaurant does not fulfill anything" in {
      when(restaurantRepo.readAll) thenReturn List(
        Restaurant("A", maxMeals = 0, maxVegetarian = 4),
        Restaurant("B", maxMeals = 7, maxVegetarian = 2, maxGlutenFree = 2)
      )

      val requirement = Requirement(others = 2, vegetarian = 1, glutenFree = 0, nutFree = 0, fishFree = 0)

      val actualOrders = orderSvc.buildOrders(requirement)

      actualOrders.exists(o => o.restaurant == "A") mustBe false
    }
  }
}
