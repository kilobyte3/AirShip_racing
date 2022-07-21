package apps._game.playground.items;

import apps.FileStorage;
import java.applet.AudioClip;

/**
 * egy hang elt�rol�sa
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
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
     * hang bet�lt�se
     */
    public void init(String source)
    {
        sound = FileStorage.loadSound(source);
    }

    /**
     * bet�lt�tt hang lej�tsz�sa
     */
    public void play()
    {
        getSound().play();
    }

    /**
     * bet�lt�tt hang lek�rdez�se
     */
    public AudioClip getSound()
    {
        return sound;
    }
}