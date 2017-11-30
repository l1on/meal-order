package models

/**
  * A helper class modeling a requirement. It makes sure none of the meal numbers is negative.
  */
case class Requirement(others: Int, vegetarian: Int, glutenFree: Int, nutFree: Int, fishFree: Int) {
  require(!(others < 0) && !(vegetarian < 0) && !(glutenFree < 0) && !(nutFree < 0) && !(fishFree < 0))
}


