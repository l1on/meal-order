package models

case class Restaurant(name: String, rating: Int = 3, maxMeals: Int, maxVegetarian: Int = 0, maxGlutenFree: Int = 0, maxNutFree: Int = 0, maxFishFree: Int = 0) {
  def fulfill(requirement: Requirement): Requirement = {
    val prioritizedReqList = List(
      ("others", requirement.others),
      ("vegetarian", requirement.vegetarian),
      ("glutenFree", requirement.glutenFree),
      ("nutFree", requirement.nutFree),
      ("fishFree", requirement.fishFree),
    ).sortBy(-_._2)


    fulfillRecur(prioritizedReqList, maxMeals, requirement)
  }

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

  private def fulfillResult(maxAmountAllowed: Int, actualAmountRequired: Int, maxMeals: Int): (Int, Int) = {
    if (maxAmountAllowed - actualAmountRequired >= 0) (maxMeals - actualAmountRequired, 0)
    else (maxMeals - maxAmountAllowed, actualAmountRequired - maxAmountAllowed)
  }
}
