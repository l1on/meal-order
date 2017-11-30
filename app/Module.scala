import com.google.inject.AbstractModule
import repositories.restaurants.{InMemoryRestaurantRepository, RestaurantRepository}

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[RestaurantRepository]).to(classOf[InMemoryRestaurantRepository])
  }
}