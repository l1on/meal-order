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

      val actualOrders = orderSvc.createOrders(requirement)

      actualOrders mustEqual List(Order("A", 36, 4, 0, 0, 0), Order("B", 2, 1, 7, 0, 0))
    }
  }
}
