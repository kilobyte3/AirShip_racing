package apps._game.playground.items;

import apps.FileStorage;
import java.applet.AudioClip;

/**
 * egy hang eltárolása
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public final class SoundEffect
{
    private AudioClip sound = null;
    private final VisualItem obj;

    /**
     * konstruktor
     */
    public SoundEffect(VisualItem obj)
    {
        this.obj = obj;
    }

    /**
     * hang betöltése
     */
    public void init(String source)
    {
        sound = FileStorage.loadSound(source);
    }

    /**
     * betöltött hang lejátszása
     */
    public void play()
    {
        getSound().play();
    }

    /**
     * betöltött hang lekérdezése
     */
    public AudioClip getSound()
    {
        return sound;
    }
}