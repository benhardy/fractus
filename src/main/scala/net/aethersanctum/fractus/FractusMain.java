package net.aethersanctum.fractus;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: Dec 24, 2009
 * Time: 5:03:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FractusMain {

    private final static int DEFAULT_IMAGE_WIDTH = 1000;
    private final static int DEFAULT_IMAGE_HEIGHT = 1000;

    public static void main(String[]argv        )  {
            System.out.println("args supplied: "+argv.length );
            if (argv.length!=1 && argv.length!=3) {
                System.err.println("Must supply a ruleset class argument");
                System.exit(-1);
            }
            RuleSet rules = new RuleSetFinder().find(argv[0]);

            int iwidth = DEFAULT_IMAGE_WIDTH;
            int iheight = DEFAULT_IMAGE_HEIGHT;
            if (argv.length==3) {
                iwidth = Integer.parseInt( argv[1] );
                iheight= Integer.parseInt( argv[2] );
            }

        Fractus mainwin = new Fractus(rules,iwidth,iheight);
        mainwin.pack();
        mainwin.setVisible(true);
        System.out.println("Main window created");
        mainwin.startThreads();


    }
}
