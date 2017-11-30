package services

import javax.inject.Inject

import models.{Order, Requirement, Restaurant}
import repositories.restaurants.RestaurantRepository

class OrderService @Inject()(restaurantRepo: RestaurantRepository) {

  def createOrders(requirement: Requirement): List[Order] = {
    val restaurantsSorted = restaurantRepo.readAll.sortBy(-_.rating)

    val orders = createOrdersRecur(restaurantsSorted, requirement)

    if (ordersMeetRequirements(orders, requirement)) orders
    else List()
  }

  private def createOrdersRecur(restaurants: List[Restaurant], requirement: Requirement): List[Order] = restaurants match {
    case Nil => List()
    case head::tail => {
      if (requirement.others + requirement.vegetarian + requirement.glutenFree + requirement.nutFree + requirement.fishFree > 0) {
        val newRequirement = head.fulfill(requirement)

        val vegetarianFulfilled = requirement.vegetarian - newRequirement.vegetarian
        val glutenFreeFulfilled = requirement.glutenFree - newRequirement.glutenFree
        val nutFreeFulfilled = requirement.nutFree - newRequirement.nutFree
        val fishFreeFulfilled = requirement.fishFree - newRequirement.fishFree
        val othersFulfilled = requirement.others - newRequirement.others

        if (newRequirement == requirement) createOrdersRecur(tail, newRequirement)
        else Order(head.name, othersFulfilled, vegetarianFulfilled, glutenFreeFulfilled, nutFreeFulfilled, fishFreeFulfilled) :: createOrdersRecur(tail, newRequirement)
      } else List()
    }
  }

  private def ordersMeetRequirements(orders: List[Order], requirement: Requirement): Boolean = {
    val fulfilledRequirement = orders.foldLeft(Requirement(0, 0, 0, 0, 0)) {
      (fulfilledRequirement, order) => Requirement(
        order.others + fulfilledRequirement.others,
        order.vegetarian + fulfilledRequirement.vegetarian,
        order.glutenFree + fulfilledRequirement.glutenFree,
        order.nutFree + fulfilledRequirement.nutFree,
        order.fishFree + fulfilledRequirement.fishFree,
      )
    }
    fulfilledRequirement == requirement
  }
}
