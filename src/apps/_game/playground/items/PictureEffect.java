package apps._game.playground.items;

import apps.FileStorage;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * egy kép eltárolása
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public final class PictureEffect
{
    private BufferedImage picture = null;
    private final VisualItem obj;

    /**
     * konstruktor
     */
    public PictureEffect(VisualItem obj)
    {
        this.obj = obj;
    }

    /**
     * kép betöltése, méreteinek eltárolása
     */
    public PictureEffect init(String source)
    {
        this.picture = FileStorage.loadPicture(source);
        obj.setCachedWidth(getPicture().getWidth());
        obj.setCachedHeight(getPicture().getHeight());
        return this;
    }

    /**
     * kép kirajzolása adott felületre és pozicióra
     */
    public void draw(Graphics at, int x, int y)
    {
        at.drawImage(getPicture(), x,y, null);
    }

    /**
     * kép lekérdezése
     */
    public BufferedImage getPicture()
    {
        return picture;
    }
}