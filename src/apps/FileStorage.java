package apps;

import general.GeneralFile;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 * az általános "GeneralFile" osztály becsomagolása, hibaellenörzéssel.
 * hiba esetén azonnal bezárja az applikációt
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public final class FileStorage
{
    /**
     * nem példányosíthatjuk!
     */
    private FileStorage()
    {
    }

    /**
     * @see GeneralFile.loadPicture
     */
    public static BufferedImage loadPicture(String fileName)
    {
        final BufferedImage result = GeneralFile.loadPicture(fileName);
        if (result == null)
        {
            JOptionPane.showMessageDialog(GeneralFile.comp, fileName + " betöltése nem sikerült.", "", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        return result;
    }

    /**
     * @see GeneralFile.loadSound
     */
    public static AudioClip loadSound(String fileName)
    {
        final AudioClip result = GeneralFile.loadSound(fileName);
        if (result == null)
        {
            JOptionPane.showMessageDialog(GeneralFile.comp, fileName + " betöltése nem sikerült.", "", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        return result;
    }

    /**
     * @see GeneralFile.loadMusic
     */
    public static URL loadMusic(String fileName)
    {
        final URL result = GeneralFile.loadMusic(fileName);
        if (result == null)
        {
            JOptionPane.showMessageDialog(GeneralFile.comp, fileName + " betöltése nem sikerült.", "", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        return result;
    }
}