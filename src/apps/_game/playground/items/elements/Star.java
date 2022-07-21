package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * csillag
 *
 * Created on 2008. m�rcius 1., 12:39
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Star extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/star.gif", 9,2, 4);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)-1);
    }
}