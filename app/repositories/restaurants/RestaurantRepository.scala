package repositories.restaurants

import models.Restaurant

/**
  * An interface for the underlying restaurant storage.
  */
trait RestaurantRepository {
  def readAll: List[Restaurant]
}
