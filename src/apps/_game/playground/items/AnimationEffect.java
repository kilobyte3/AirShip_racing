package apps._game.playground.items;

import apps.Animation;
import java.awt.Graphics;

/**
 * egy anim�ci� elt�rol�sa
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
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
     * anim�ci� bet�lt�se
     *
     * @param fname f�jlb�l
     * @param height anim�ci�s frame magass�ggal
     * @param count forr�sk�pen h�ny anim�ci�s elem van
     * @param speed anim�l�si sebess�ggel
     */
    public AnimationEffect init(String fname, int height, int count, int speed)
    {
        animation = new Animation(fname, height,count, speed);
        obj.setCachedWidth(getAnimation().getWidth());
        obj.setCachedHeight(getAnimation().getHeight());
        return this;
    }

    /**
     * anim�l�s
     */
    public void update()
    {
        getAnimation().update();
    }

    /**
     * kirajzol�s, aktu�lis anim�ci�s f�zisnak megfelel�en
     */
    public void draw(Graphics at, int x, int y)
    {
        getAnimation().draw(at, x,y);
    }

    /**
     * anim�ci� lek�rdez�se
     */
    public Animation getAnimation()
    {
        return animation;
    }
}