package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * sas
 *
 * Created on 2008. március 2., 19:07
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Eagle extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y, 3,1);
        getAnimationInstance().init("Gfx/eagle.gif", 7,4, 2);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)-1);
    }
}