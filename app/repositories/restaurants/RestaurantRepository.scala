package repositories.restaurants

import models.Restaurant

trait RestaurantRepository {
  def readAll: List[Restaurant]
}
