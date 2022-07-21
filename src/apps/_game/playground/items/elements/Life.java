package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * �let
 *
 * Created on 2008. m�rcius 2., 17:39
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Life extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/1up.gif", 18, 3, 4);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)1).setRemoveable();
    }
}