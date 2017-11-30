package models

import org.scalatestplus.play.PlaySpec

class RequirementSpec extends PlaySpec {
  "Requirement" should {
    "throw IllegalArgumentException when one of the meal number is negative" in {
      an [IllegalArgumentException] should be thrownBy Requirement(3, -1, 10, 2, 0)
    }

    "not throw an exception when all the meal numbers are 0" in {
      noException must be thrownBy Requirement(0, 0, 0, 0, 0)
    }

    "not throw an exception when none of the meal numbers is negative" in {
      noException must be thrownBy Requirement(1, 0, 5, 0, 0)
    }
  }
}
