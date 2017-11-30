package controllers

import javax.inject._

import play.api.mvc._
import services.OrderService
import models.{Order, Requirement}
import play.api.libs.json.Json

@Singleton
class OrdersController @Inject()(cc: ControllerComponents, orderSvc: OrderService) extends AbstractController(cc) {
  implicit val orderWrites = Json.writes[Order]

  def create(total: Int, vegetarian: Int, gluten_​free: Int, nut_free: Int, fish_free: Int) = Action { implicit request: Request[AnyContent] =>
    val others = total - (vegetarian + gluten_​free + nut_free + fish_free)

    try {
      val requirement = Requirement(others, vegetarian, gluten_​free, nut_free, fish_free)
      Ok(Json.toJson(orderSvc.createOrders(requirement)))
    } catch {
      case e: IllegalArgumentException => BadRequest("Invalid Meal Request")
    }
  }
}
