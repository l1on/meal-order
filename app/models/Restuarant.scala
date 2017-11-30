package models

case class Restaurant(name: String, rating: Int = 3, maxMeals: Int, maxVegetarian: Int = 0, maxGlutenFree: Int = 0, maxNutFree: Int = 0, maxFishFree: Int = 0) {
  /**
    * fulfill method takes a requirement and prioritizes all the special meals over the general meals. Among the special
    * meals, it prioritizes those with a higher number of requests. The method returns a new requirement after the
    * restaurant has fulfilled the original requirement to its best ability.
    */
  def fulfill(requirement: Requirement): Requirement = {
    val prioritizedReqList = List(
      ("vegetarian", requirement.vegetarian),
      ("glutenFree", requirement.glutenFree),
      ("nutFree", requirement.nutFree),
      ("fishFree", requirement.fishFree),
    ).sortBy(-_._2) :+  ("others", requirement.others)

    fulfillRecur(prioritizedReqList, maxMeals, requirement)
  }

  /**
    * This recursive helper method recurs on a list of meal requests. It works on the first meal request in the list,
    * tries to fulfill that particular type of meal and then passes on the requests for other types of meals, along
    * with a new requirement, to itself again. The recursion stops as soon as there's no request for any type of meals.
    */
  private def fulfillRecur(reqList: List[(String, Int)], maxMeals: Int, requirement: Requirement): Requirement = reqList match {
    case Nil => requirement
    case head::tail => head._1 match {
      case "others" => {
        val (remainingMaxMeal, remainingOthers) = fulfillResult(maxMeals, head._2, maxMeals)

        fulfillRecur(tail, remainingMaxMeal, requirement.copy(others = remainingOthers))
      }
      case "vegetarian" => {
        val actualMaxVegetarian = Math.min(maxMeals, maxVegetarian)

        val (remainingMaxMeal, remainingVegetarian) = fulfillResult(actualMaxVegetarian, head._2, maxMeals)

        fulfillRecur(tail, remainingMaxMeal, requirement.copy(vegetarian = remainingVegetarian))
      }
      case "glutenFree" => {
        val actualMaxGlutenFree = Math.min(maxMeals, maxGlutenFree)

        val (remainingMaxMeal, remainingGlutenFree) = fulfillResult(actualMaxGlutenFree, head._2, maxMeals)

        fulfillRecur(tail, remainingMaxMeal, requirement.copy(glutenFree = remainingGlutenFree))
      }
      case "nutFree" => {
        val actualMaxNutFree = Math.min(maxMeals, maxNutFree)

        val (remainingMaxMeal, remainingNutFree) = fulfillResult(actualMaxNutFree, head._2, maxMeals)

        fulfillRecur(tail, remainingMaxMeal, requirement.copy(nutFree = remainingNutFree))
      }
      case "fishFree" => {
        val actualMaxFishFree = Math.min(maxMeals, maxFishFree)

        val (remainingMaxMeal, remainingFishFree) = fulfillResult(actualMaxFishFree, head._2, maxMeals)

        fulfillRecur(tail, remainingMaxMeal, requirement.copy(fishFree = remainingFishFree))
      }
    }
  }

  /**
    * A helper to calculate the remaining max meal (for the restaurant) and the remaining unfulfilled amount for a
    * particular type of meal after the restaurant tried to fulfill a request for that type of meal.
    */
  private def fulfillResult(maxAmountAllowed: Int, actualAmountRequired: Int, maxMeals: Int): (Int, Int) = {
    if (maxAmountAllowed - actualAmountRequired >= 0) (maxMeals - actualAmountRequired, 0)
    else (maxMeals - maxAmountAllowed, actualAmountRequired - maxAmountAllowed)
  }
}
