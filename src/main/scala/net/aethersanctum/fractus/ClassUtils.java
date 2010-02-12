package net.aethersanctum.fractus;

/*
public class ClassUtils {
    public static final String PACKAGE = "net.aethersanctum.fractus.ruleset";

    public static Class findClass(String className) throws ClassNotFoundException {
        Class c;
        try {
            System.err.println("looking for full class "+className);
            c = Class.forName(className);
        }
        catch(ClassNotFoundException e) {
            className = PACKAGE + "." + className;
            System.err.println("no dice\nlooking for package-prepended class "+className);
            c = Class.forName(className);
        }
        System.err.println("Gotcha!");
        return c;
    }

   public static RuleSet findInnerClass(String className) throws Exception {
        System.err.println("Attempting to discover if "+className+" is an inner class");
        int p = className.lastIndexOf('$');
        if (p==-1)
            throw new ClassNotFoundException("it ain't an inner class either");
        String parent = className.substring(0, p);
        System.err.println("Trying with parent class name of: "+parent);
        Class parentClass = Class.forName(parent);
        System.err.println("Found parent class name of: "+parentClass.getCanonicalName());
        Object parentInstance = parentClass.newInstance();
        Class[] innerClasses = parentInstance.getClass().getClasses();

        System.err.println("Examining inner classes");
        for(int i=0;i<innerClasses.length;i++){
            if(innerClasses[i].getName().equals(className)){
                return (RuleSet)innerClasses[i].getConstructor(new Class[]{parentClass}).newInstance(new Object[]{parentInstance});
            }
        }
        throw new ClassNotFoundException(className+" isn't an inner class");
    }

    public static RuleSet getRuleSet( String className ) {
        RuleSet r;
        System.err.println("Looking for RuleSet class "+className);
        try {
            r = (RuleSet)( findClass(className).newInstance());
        }
        catch(Exception e ) {
            e.printStackTrace();
            try {
                System.err.println("Attempting to find container class without package added");
                r = findInnerClass(className);
            }
            catch( Exception e3) {
                e.printStackTrace();
                System.err.println("Attempting to find container class with package added");
                try { r = findInnerClass(PACKAGE + "."  + className);
                }
                catch( Exception e4) {
                    // well shit... maybe this is an inner class?
                    throw new IllegalArgumentException("Couldn't find the ruleset class whether i stuck package name on the front or not:"+className,e3);

                }
            }
        }
        System.err.println("Using RuleSet class "+r.getClass().getName());
        return r;
    }

}
*/