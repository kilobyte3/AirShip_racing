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
 * f�jlok bet�lt�s�t �s nyilv�ntart�s�t szolg�l� oszt�ly
 * �ltal�nos, m�shol is fel lehet haszn�lni, b�r k�zvetlen�l nem �rdemes, jobb becsomagolni egy hibaellen�rz�se v�gz� oszt�lyba
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class GeneralFile
{
    private static Map<String, BufferedImage> dictionaryPicture;
    private static Map<String, AudioClip> dictionarySound;
    private static Map<String, URL> dictionaryMusic;
    public static Component comp;

    /**
     * nem p�ld�nyos�thatjuk!
     */
    private GeneralFile()
    {
    }

    /**
     * ellen�rzi, hogy a "fileName" k�p m�r be van e t�ltve, �s visszaadja. Ha nincs, null-t ad vissza
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
     * ellen�rzi, hogy a "fileName" hang m�r be van e t�ltve, �s visszaadja. Ha nincs, null-t ad vissza
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
     * ellen�rzi, hogy a "fileName" zene m�r be van e t�ltve, �s visszaadja. Ha nincs, null-t ad vissza
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
     * adott sz�t�rb�l lek�rdezi a "fileName" cuccost
     */
    private static Object getFromDictionary(Map source, String fileName)
    {
        return source.get(fileName);
    }

    /**
     * k�p bet�lt�se �s visszaad�sa, sikertelen esetben null
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
     * hang bet�lt�se �s visszaad�sa, sikertelen esetben null
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
     * visszaadja "fileName"-t URL objektumban, ak�r k�ls�, ak�r bels�.
     * ha neml�tezik, null a visszat�r�si �rt�k
     */
    public static URL getFile(String fileName)
    {
        URL item = null;
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory() || !file.canRead())
        {
            // tal�n a bels� JAR �llom�nyban van a f�jl:
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
     * l�tezik a f�jl?
     */
    public static boolean itemExists(String fileName)
    {
        return getFile(fileName) != null;
    }

    /**
     * zene bet�lt�se �s visszaad�sa, sikertelen esetben null
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