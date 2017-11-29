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
    "include the correct orders according to the example" in {
      val others = 38
      val vegetarian = 5
      val glutenFree = 7
      val nutFree = 0
      val fishFree = 0
      val requirement = Requirement(others, vegetarian, glutenFree, nutFree, fishFree)

      val restaurants = List(
        Restaurant("A", 5, maxMeals=40),
        Restaurant("B", 3, maxMeals=100, maxVegetarian=20, maxGlutenFree=20)
      )

      when(restaurantRepo.readAll) thenReturn restaurants

      val actualOrders = orderSvc.createOrders(requirement)

      actualOrders mustEqual List(Order("A", 36, 4, 0, 0, 0), Order("B", 2, 1, 7, 0, 0))
    }

  }
}
