package controllers

import javax.inject._

import play.api.mvc._
import services.OrderService
import models.Requirement



@Singleton
class OrdersController @Inject()(cc: ControllerComponents, orderSvc: OrderService) extends AbstractController(cc) {

  def create(total: Int, vegetarian: Int, gluten_​free: Int, nut_free: Int, fish_free: Int) = Action { implicit request: Request[AnyContent] =>
    val others = total - (vegetarian + gluten_​free + nut_free + fish_free)
    orderSvc.createOrders(Requirement(others, vegetarian, gluten_​free, nut_free, fish_free))

    Ok(views.html.index())
  }
}
