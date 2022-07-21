package general;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class ClassFinderInPackage
{
    /**
     * Given a package name, attempts to reflect to find all classes within the package
     * on the local file system.
     *
     * @param packageName
     * @return
     */
    public static ArrayList<String> getClassesInPackage(String packageName)
    {
        ArrayList classes = new ArrayList<String>();
        String packageNameSlashed = packageName.replace(".", "/");
        // Get a File object for the package
        URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);
        if (directoryURL == null) {
            //LOG.warn("Could not retrieve URL resource: " + packageNameSlashed);
            return classes;
        }

        String directoryString = directoryURL.getFile();
        if (directoryString == null) {
            //LOG.warn("Could not find directory for URL resource: " + packageNameSlashed);
            return classes;
        }

        File directory = new File(directoryString);
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for(String fileName : files) {
                // We are only interested in .class files
                if (fileName.endsWith(".class")) {
                    // Remove the .class extension
                    fileName = fileName.substring(0, fileName.length() - 6);
                    try {
                        classes.add(Class.forName(packageName + "." + fileName).getName().substring(packageName.length()+1));
                    } catch(ClassNotFoundException e) {
                        //LOG.warn(packageName + "." + fileName + " does not appear to be a valid class.", e);
                    }
                }
            }
        } else {
            //LOG.warn(packageName + " does not appear to exist as a valid package on the file system.");
        }
        return classes;
    }
}