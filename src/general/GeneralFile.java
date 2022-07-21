package general;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * fájlok betöltését és nyilvántartását szolgáló osztály
 * általános, máshol is fel lehet használni, bár közvetlenül nem érdemes, jobb becsomagolni egy hibaellenörzése végzõ osztályba
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class GeneralFile
{
    private static Map<String, BufferedImage> dictionaryPicture;
    private static Map<String, AudioClip> dictionarySound;
    private static Map<String, URL> dictionaryMusic;
    public static Component comp;

    /**
     * nem példányosíthatjuk!
     */
    private GeneralFile()
    {
    }

    /**
     * ellenõrzi, hogy a "fileName" kép már be van e töltve, és visszaadja. Ha nincs, null-t ad vissza
     */
    private static BufferedImage checkPicture(String fileName)
    {
        if (dictionaryPicture == null)
        {
            dictionaryPicture = new HashMap<String, BufferedImage>();
            return null;
        }
        return (BufferedImage)getFromDictionary(dictionaryPicture, fileName);
    }

    /**
     * ellenõrzi, hogy a "fileName" hang már be van e töltve, és visszaadja. Ha nincs, null-t ad vissza
     */
    private static AudioClip checkSound(String fileName)
    {
        if (dictionarySound == null)
        {
            dictionarySound = new HashMap<String, AudioClip>();
            return null;
        }
        return (AudioClip)getFromDictionary(dictionarySound, fileName);
    }

    /**
     * ellenõrzi, hogy a "fileName" zene már be van e töltve, és visszaadja. Ha nincs, null-t ad vissza
     */
    private static URL checkMusic(String fileName)
    {
        if (dictionaryMusic == null)
        {
            dictionaryMusic = new HashMap<String, URL>();
            return null;
        }
        return (URL)getFromDictionary(dictionaryMusic, fileName);
    }

    /**
     * adott szótárból lekérdezi a "fileName" cuccost
     */
    private static Object getFromDictionary(Map source, String fileName)
    {
        return source.get(fileName);
    }

    /**
     * kép betöltése és visszaadása, sikertelen esetben null
     */
    public static BufferedImage loadPicture(String fileName)
    {
        BufferedImage alreadyLoadedItem = checkPicture(fileName);
        if (alreadyLoadedItem != null)
        {
            return alreadyLoadedItem;
        }

        URL resourceURL = getFile(fileName);
        if (resourceURL == null)
        {
            return null;
        }
        try
        {
            alreadyLoadedItem = ImageIO.read(resourceURL);
        }
        catch(IOException ex)
        {
            Logger.getLogger(GeneralFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        dictionaryPicture.put(fileName, alreadyLoadedItem);

        return alreadyLoadedItem;
    }

    /**
     * hang betöltése és visszaadása, sikertelen esetben null
     */
    public static AudioClip loadSound(String fileName)
    {
        AudioClip alreadyLoadedItem = checkSound(fileName);
        if (alreadyLoadedItem != null)
        {
            return alreadyLoadedItem;
        }

        URL resourceURL = getFile(fileName);
        if (resourceURL == null)
        {
            return null;
        }
        alreadyLoadedItem = Applet.newAudioClip(resourceURL);

        dictionarySound.put(fileName, alreadyLoadedItem);

        return alreadyLoadedItem;
    }

    /**
     * visszaadja "fileName"-t URL objektumban, akár külsõ, akár belsõ.
     * ha nemlétezik, null a visszatérési érték
     */
    public static URL getFile(String fileName)
    {
        URL item = null;
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory() || !file.canRead())
        {
            // talán a belsõ JAR állományban van a fájl:
            item = comp.getClass().getClassLoader().getResource(fileName);
        }
        else
        {
            try
            {
                item = file.toURL();
            }
            catch(MalformedURLException ex)
            {
                item = null;
            }
        }
        return item;
    }

    /**
     * létezik a fájl?
     */
    public static boolean itemExists(String fileName)
    {
        return getFile(fileName) != null;
    }

    /**
     * zene betöltése és visszaadása, sikertelen esetben null
     */
    public static URL loadMusic(String fileName)
    {
        URL alreadyLoadedItem = checkMusic(fileName);
        if (alreadyLoadedItem != null)
        {
            return alreadyLoadedItem;
        }

        alreadyLoadedItem = getFile(fileName);
        if (alreadyLoadedItem == null)
        {
            return null;
        }

        dictionaryMusic.put(fileName, alreadyLoadedItem);

        return alreadyLoadedItem;
    }
}