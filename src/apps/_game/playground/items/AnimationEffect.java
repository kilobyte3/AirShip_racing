package apps._game.playground.items;

import apps.Animation;
import java.awt.Graphics;

/**
 * egy animáció eltárolása
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public final class AnimationEffect
{
    private Animation animation = null;
    private final VisualItem obj;

    /**
     * konstruktor
     */
    public AnimationEffect(VisualItem obj)
    {
        this.obj = obj;
    }

    /**
     * animáció betöltése
     *
     * @param fname fájlból
     * @param height animációs frame magassággal
     * @param count forrásképen hány animációs elem van
     * @param speed animálási sebességgel
     */
    public AnimationEffect init(String fname, int height, int count, int speed)
    {
        animation = new Animation(fname, height,count, speed);
        obj.setCachedWidth(getAnimation().getWidth());
        obj.setCachedHeight(getAnimation().getHeight());
        return this;
    }

    /**
     * animálás
     */
    public void update()
    {
        getAnimation().update();
    }

    /**
     * kirajzolás, aktuális animációs fázisnak megfelelõen
     */
    public void draw(Graphics at, int x, int y)
    {
        getAnimation().draw(at, x,y);
    }

    /**
     * animáció lekérdezése
     */
    public Animation getAnimation()
    {
        return animation;
    }
}