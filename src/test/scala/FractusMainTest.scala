package net.aethersanctum.fractus

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class FractusMainTest extends FunSuite with ShouldMatchers {

  test("main app should proceed without command line parameters") {
    val args = Array[String]()
    FractusMain.checkArgumentListLength(args)
  }

  test("main app should accept just fractal fractalName command line parameter") {
    val args = Array("Frac")
    FractusMain.checkArgumentListLength(args)
  }

  test("main app should accept fractal fractalName and image dimensions") {
    val args = Array("Frac","1920","800")
    FractusMain.checkArgumentListLength(args)
  }

  test("main app should reject argument lists with more than 3 items") {
    val args = Array("Frac","1920","800","what is this")
    intercept[IllegalArgumentException] {
      FractusMain.checkArgumentListLength(args)
    }
  }

  test("main app should reject argument lists with only 2 items") {
    val args = Array("Frac","1920")
    intercept[IllegalArgumentException] {
      FractusMain.checkArgumentListLength(args)
    }
  }
  
  test("main app should use default image dimensions if none are supplied") {
    val args = Array("Sierpinski")
    val dims = FractusMain.determineImageDimensions(args)
    dims should be === (FractusMain.DEFAULT_IMAGE_WIDTH, FractusMain.DEFAULT_IMAGE_HEIGHT)
  }

  test("main app should use supplied image dimensions where they exist") {
    val args = Array("Sierpinski", "1920", "1800")
    val dims = FractusMain.determineImageDimensions(args)
    dims should be === (1920, 1800)
  }
}