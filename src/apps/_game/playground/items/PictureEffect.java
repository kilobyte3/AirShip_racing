package apps._game.playground.items;

import apps.FileStorage;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * egy k�p elt�rol�sa
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
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
     * k�p bet�lt�se, m�reteinek elt�rol�sa
     */
    public PictureEffect init(String source)
    {
        this.picture = FileStorage.loadPicture(source);
        obj.setCachedWidth(getPicture().getWidth());
        obj.setCachedHeight(getPicture().getHeight());
        return this;
    }

    /**
     * k�p kirajzol�sa adott fel�letre �s pozici�ra
     */
    public void draw(Graphics at, int x, int y)
    {
        at.drawImage(getPicture(), x,y, null);
    }

    /**
     * k�p lek�rdez�se
     */
    public BufferedImage getPicture()
    {
        return picture;
    }
}