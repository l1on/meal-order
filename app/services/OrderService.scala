package services

import javax.inject.Inject

import models.{Order, Requirement, Restaurant}
import repositories.restaurants.RestaurantRepository

class OrderService @Inject()(restaurantRepo: RestaurantRepository) {
  /**
    * buildOrders ranks in desc order all the restaurants by their ratings,
    * then delegates the actual order generation to a helper method. In case the user requirement cannot
    * be fully fulfilled, it returns an empty order list.
    */
  def buildOrders(requirement: Requirement): List[Order] = {
    val restaurantsSorted = restaurantRepo.readAll.sortBy(-_.rating)

    val orders = buildOrdersRecur(restaurantsSorted, requirement)

    if (ordersMeetRequirements(orders, requirement)) orders
    else List()
  }

  /**
    * A recursive helper method that actually builds a list of orders. It recurs on a list of restaurants. 'head' is the first
    * restaurant in the list and 'tail' is all the following restaurants. As long as a requirement hasn't been fully fulfilled,
    * it will let the first restaurant fulfill the requirement and the rest of the restaurants handle any requirement that
    * was a left-over from the first restaurant. The recursion stops as soon as there's either no restaurant or no requirement.
    *
    * Only after the first restaurant has at least partially fulfilled the requirement, will an order with its name and the fulfilled amount
    * be put into the order list.
    */
  private def buildOrdersRecur(restaurants: List[Restaurant], requirement: Requirement): List[Order] = restaurants match {
    case Nil => List()
    case head::tail => {
      if (requirement.others + requirement.vegetarian + requirement.glutenFree + requirement.nutFree + requirement.fishFree > 0) {
        val newRequirement = head.fulfill(requirement)

        val vegetarianFulfilled = requirement.vegetarian - newRequirement.vegetarian
        val glutenFreeFulfilled = requirement.glutenFree - newRequirement.glutenFree
        val nutFreeFulfilled = requirement.nutFree - newRequirement.nutFree
        val fishFreeFulfilled = requirement.fishFree - newRequirement.fishFree
        val othersFulfilled = requirement.others - newRequirement.others

        if (newRequirement == requirement) buildOrdersRecur(tail, newRequirement)
        else Order(head.name, othersFulfilled, vegetarianFulfilled, glutenFreeFulfilled, nutFreeFulfilled, fishFreeFulfilled) :: buildOrdersRecur(tail, newRequirement)
      } else List()
    }
  }

  /**
    * A helper boolean method that checks whether a generated list of orders actually fully fulfills a requirement.
    */
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
