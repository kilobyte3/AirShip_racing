package apps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * képekbõl készithetünk animációkat. Minden frame egyforma méretû és felülrõl lefelé van definiálva!
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class Animation
{
    final private int height, count, speed;
    final private BufferedImage source;
    private int tick, frame;

    /**
     * animáció meghatározása:
     * @param fname forráskép
     * @param height maga az animáció magassága
     * @param count hány darab fázist definiál a forrásfájl
     * @param speed animálási sebesség
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
     * animáció éltetése
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
                frame = 0; // ha végetért az animáció, elõrõl kezdjük
            }
        }
    }

    /**
     * animáció kirajzolása adott felületre és pozicióra
     */
    public void draw(Graphics drawto, int x, int y)
    {
        drawto.drawImage(source, x, y, x+source.getWidth(), y+height, 0, height*frame, source.getWidth(), (height*frame)+height, null,null);
    }

    /**
     * animációs fázis beállítása egy "kockára"
     */
    public final void setFrameAt(int at)
    {
        tick = 0;
        frame = 0;
    }

    /**
     * animáció magassága
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * animációs fázisok száma
     */
    public int getCount()
    {
        return count;
    }

    /**
     * animációs fázis index
     */
    public int getFrame()
    {
        return frame;
    }

    /**
     * animáció sebessége
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * animáció szélessége
     */
    public int getWidth()
    {
        return source.getWidth();
    }
}