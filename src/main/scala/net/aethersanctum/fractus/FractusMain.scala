package net.aethersanctum.fractus


object FractusMain {

    private val DEFAULT_IMAGE_WIDTH = 1000
    private val DEFAULT_IMAGE_HEIGHT = 1000

    def main(argv:Array[String]) {
        println("args supplied: " + argv.length)
        if (argv.length != 1 && argv.length != 3) {
            System.err.println("USAGE: fractus.sh FRACTALNAME [width height]")
            System.exit(-1)
        }
        val rules = new RuleSetFinder().find(argv(0))

        val (iwidth, iheight) = if (argv.length == 3) {
            (Integer parseInt argv(1), Integer parseInt argv(2))
        } else {
            (DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT)
        }
        val mainwin = new Fractus(rules, iwidth, iheight)
        mainwin.pack()
        mainwin setVisible true
        mainwin startThreads
    }
}
