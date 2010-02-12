package net.aethersanctum.fractus


object FractusMain {

    private val DEFAULT_IMAGE_WIDTH = 1000
    private val DEFAULT_IMAGE_HEIGHT = 1000

    def main(argv:Array[String]) {
        println("args supplied: " + argv.length)
        if (argv.length != 1 && argv.length != 3) {
            System.err.println("Must supply a ruleset class argument")
            System.exit(-1)
        }
        val rules = new RuleSetFinder().find(argv(0))

        var iwidth = DEFAULT_IMAGE_WIDTH;
        var iheight = DEFAULT_IMAGE_HEIGHT;
        if (argv.length == 3) {
            iwidth = Integer.parseInt(argv(1))
            iheight = Integer.parseInt(argv(2))
        }

        val mainwin = new Fractus(rules, iwidth, iheight)
        mainwin.pack()
        mainwin setVisible true
        mainwin startThreads
    }
}
