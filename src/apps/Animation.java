package apps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * k�pekb�l k�szithet�nk anim�ci�kat. Minden frame egyforma m�ret� �s fel�lr�l lefel� van defini�lva!
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class Animation
{
    final private int height, count, speed;
    final private BufferedImage source;
    private int tick, frame;

    /**
     * anim�ci� meghat�roz�sa:
     * @param fname forr�sk�p
     * @param height maga az anim�ci� magass�ga
     * @param count h�ny darab f�zist defini�l a forr�sf�jl
     * @param speed anim�l�si sebess�g
     */
    public Animation(String fname, int height, int count, int speed)
    {
        this.source = FileStorage.loadPicture(fname);
        this.count = count;
        this.height = height;
        this.speed = speed;
        setFrameAt(0);
    }

    public Animation(String fileName, int _height, int _count)
    {
        this(fileName, _height, _count, 1);
    }

    /**
     * anim�ci� �ltet�se
     */
    public void update()
    {
        tick = tick + 1;
        if (tick >= speed)
        {
            tick = 0;
            frame = frame + 1;
            if (frame >= count )
            {
                frame = 0; // ha v�get�rt az anim�ci�, el�r�l kezdj�k
            }
        }
    }

    /**
     * anim�ci� kirajzol�sa adott fel�letre �s pozici�ra
     */
    public void draw(Graphics drawto, int x, int y)
    {
        drawto.drawImage(source, x, y, x+source.getWidth(), y+height, 0, height*frame, source.getWidth(), (height*frame)+height, null,null);
    }

    /**
     * anim�ci�s f�zis be�ll�t�sa egy "kock�ra"
     */
    public final void setFrameAt(int at)
    {
        tick = 0;
        frame = 0;
    }

    /**
     * anim�ci� magass�ga
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * anim�ci�s f�zisok sz�ma
     */
    public int getCount()
    {
        return count;
    }

    /**
     * anim�ci�s f�zis index
     */
    public int getFrame()
    {
        return frame;
    }

    /**
     * anim�ci� sebess�ge
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * anim�ci� sz�less�ge
     */
    public int getWidth()
    {
        return source.getWidth();
    }
}