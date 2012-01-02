package net.aethersanctum.fractus

/**
 * Main entry point to the application. Deals with command-line parameters,
 * instantiates main application and starts it.
 */
object FractusMain {

  private val DEFAULT_IMAGE_WIDTH = 1000
  private val DEFAULT_IMAGE_HEIGHT = 1000

  def main(argv: Array[String]) {
    checkArgumentListLength(argv)
    val fractalName = argv(0)

    val (width, height) = determineImageDimensions(argv)
    val application = new FractusApp(width, height)
    application.startDrawing(fractalName)
  }

  private def checkArgumentListLength(argv: Array[String]) {
    println("args supplied: " + argv.length)
    if (argv.length != 1 && argv.length != 3) {
      System.err.println("USAGE: fractus.sh FRACTALNAME [width height]")
      System.exit(-1)
    }
  }

  private def determineImageDimensions(argv: Array[String]): (Int, Int) = {
    if (argv.length == 3) {
      (Integer parseInt argv(1), Integer parseInt argv(2))
    } else {
      (DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT)
    }
  }

}
