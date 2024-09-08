package utils;

import java.util.*;
import java.io.*;


/**
 * class [PropertyHandler]
 *
 * This is a utility class reading properties data contained in some file
 */
public class PropertyHandler extends Properties {

    File propertyFile = null;

    /**
     * Constructor
     * 
     * @param propertyFileString File name containing properties on a relative path
     * 
     * @throws java.io.FileNotFoundException
     */
    public PropertyHandler(String propertyFileString) throws FileNotFoundException, IOException {

        propertyFile = getPropertyFile(propertyFileString);

        InputStream is = new BufferedInputStream(new FileInputStream(propertyFile));
        this.load(is);
        is.close();
    }

    
    /**
     * Important method reading the properties.
     */
    @Override
    public String getProperty(String key) {
        String value = super.getProperty(key);

        return value;
    }

    
    /**
     * Private method, looking for a valid properties file in different directories
     */
    private File getPropertyFile(String propertyFileString)
            throws FileNotFoundException, IOException {

        // ... in the current directory
        if ((propertyFile = new File(propertyFileString)).exists()) {    
            //System.out.println(new File(".").getAbsolutePath());
            return propertyFile;
        }

        // ... in the directory, where the program was started
        String dirString = System.getProperty("user.dir");
        String completeString = dirString + File.separator + propertyFileString;
        if ((propertyFile = new File(completeString)).exists()) {
            return propertyFile;
        }

        // ... in Home-directory of the user
        dirString = System.getProperty("user.home");
        completeString = dirString + File.separator + propertyFileString;
        if ((propertyFile = new File(completeString)).exists()) {
            return propertyFile;
        }

        // ... in the directory where Java keeps its own property files
        dirString = System.getProperty("java.home") + File.separator + "lib";
        completeString = dirString + File.separator + propertyFileString;
        if ((propertyFile = new File(completeString)).exists()) {
            return propertyFile;
        }

        // ... in all directories specified by the CLASSPATH
        String[] classPathes = getClassPathes();
        for (int i = 0; i < classPathes.length; i++) {
            completeString = classPathes[i] + File.separator + propertyFileString;
            if ((propertyFile = new File(completeString)).exists()) {
                return propertyFile;
            }
        }
  
        throw new FileNotFoundException("[PropertyHandler.PropertyHandler] Configuration file \"" + propertyFileString + "\" not found!");
    }
    
    
    /**
     * Auxiliary function to return an array of paths from the CLASSPATH variable
     * @return 
     */
    public static String[] getClassPathes(){
            String[] classPathes = new String[1];
            String classPath = System.getProperty("java.class.path");
            //System.out.println("Klassenpfade: " + classPath);

            StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);

            int count = 0;
            String[] oldClassPathes;
            String token;
            while(tokenizer.hasMoreTokens()){
                    token = tokenizer.nextToken();
                    if(token.endsWith(File.separator))
                            token = token.substring(0, token.length()-1);
                    classPathes[count] = token;
                    oldClassPathes = classPathes;
                    classPathes = new String[++count + 1];
                    //System.out.println("piep!");
                    System.arraycopy(oldClassPathes, 0, classPathes, 0, count);
            }
            // Schrumpfen um eins
            oldClassPathes = classPathes;
            classPathes = new String[count];
            //System.out.println("piep!");
            System.arraycopy(oldClassPathes, 0, classPathes, 0, count);

            return classPathes;
    }
}
